set dotenv-load := true
APP_NAME := "query-lab-api-kt"
GIT_HASH := `git rev-parse HEAD`

DOCKER_REPO := "nnao45"
MYSQL_DOCKER := "jqapi-test-mysql"
MYSQL_DOCKER_EXISTS_FLAG := `if [ ! -z ${CIRCLECI:-} ]; then echo 1; exit 0; fi; if [ ! -z ${IS_KUBE:-} ]; then echo 1; exit 0; fi; docker ps --format "{{ .Names }}" --filter "name=jqapi-test-mysql" | wc -l`
MYSQL_VERSION := "8.0.25"

run:
    ./gradlew runMain

docker-login:
    docker login

docker-build:
    ./gradlew jibDockerBuild

docker-run:
    docker run -it --rm -p 8081:8081 --env-file=./.env {{ DOCKER_REPO }}/{{ APP_NAME }}:{{ GIT_HASH }}

docker-push: docker-login
    docker push {{ DOCKER_REPO }}/{{ APP_NAME }}:{{ GIT_HASH }}

docker-release: docker-build docker-push

docker-base-build:
    docker build -t {{ DOCKER_REPO }}/{{ APP_NAME }}:base -f base.Dockerfile .

docker-base-run:
    docker run -it --rm -p 8081:8081 --env-file=./.env {{ DOCKER_REPO }}/{{ APP_NAME }}:base

docker-base-push: docker-login
    docker push {{ DOCKER_REPO }}/{{ APP_NAME }}:base

docker-base-release: docker-base-build docker-base-push

docker-ddl-build:
	docker rmi -f {{ DOCKER_REPO }}/ddl-docker:latest
	docker build -t {{ DOCKER_REPO }}/ddl-docker:latest . -f=ddl.Dockerfile
	docker tag {{ DOCKER_REPO }}/ddl-docker:latest {{ DOCKER_REPO }}/ddl-docker:{{ GIT_HASH }}

docker-ddl-push: docker-login
	docker push {{ DOCKER_REPO }}/ddl-docker:latest
	docker push {{ DOCKER_REPO }}/ddl-docker:{{ GIT_HASH }}

docker-ddl-release: docker-ddl-build docker-ddl-push

mysql-db-is-exist:
    #!/bin/bash
    echo MYSQL_DOCKER_EXISTS_FLAG is {{ MYSQL_DOCKER_EXISTS_FLAG }}
    if [ ! {{ MYSQL_DOCKER_EXISTS_FLAG }} = 0 ]; then
        echo "{{ MYSQL_DOCKER }} is exists"
    else
        echo "{{ MYSQL_DOCKER }} is not exists"
    fi

check-mysql-db:
    #!/bin/bash
    STATUS=1
    COUNT=0

    while [ ${STATUS} = 1 ]
    do
        mysqladmin ping -u root -h ${MYSQL_HOST} -p${MYSQL_PORT} > /dev/null 2>&1 && STATUS=0
        if [ ${STATUS} = 1 ]; then
            echo -n '.'
        fi
        sleep 1
        (( COUNT++ ))
        if [ ${COUNT} = 50 ]; then
            echo "Sorry, Cannot connect the mysql docker."
            exit 1
        fi
    done

    echo '!'
    echo {{ MYSQL_DOCKER }} is alive

lunch-mysql-db:
    #!/bin/bash
    if [ ! {{ MYSQL_DOCKER_EXISTS_FLAG }} = 0 ]; then
        echo "{{ MYSQL_DOCKER }} is exists. Not work."
    else
        echo 'set up the mysql docker'
        docker run --name {{ MYSQL_DOCKER }} \
            --rm \
            -d \
            -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
            -p ${MYSQL_PORT}:${MYSQL_PORT} mysql:{{ MYSQL_VERSION }} \
            --character-set-server=utf8mb4 \
            --default-authentication-plugin=mysql_native_password \
            --collation-server=utf8mb4_unicode_ci
    fi

setup-mysql-db:
    echo "CREATE DATABASE IF NOT EXISTS ${MYSQL_DB} DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci" | mysql -h${MYSQL_HOST} -u${MYSQL_ROOT}
    echo 'CREATE USER '\'admin\''@'\'%\'' IDENTIFIED BY '\'${MYSQL_PASSWORD}\''' | mysql -h${MYSQL_HOST} -u${MYSQL_ROOT}
    echo 'GRANT ALL On *.* To admin@'\'%\'';' | mysql -h${MYSQL_HOST} -u${MYSQL_ROOT}
    echo 'set global time_zone = "Asia/Tokyo"' | mysql -h${MYSQL_HOST} -u${MYSQL_USER} -p${MYSQL_PASSWORD}
    ./gradlew runMigrate

inject-mysql-db:
    mysql_random_data_load ${MYSQL_DB} user       100000 --user=admin --password=mypwds
    mysql_random_data_load ${MYSQL_DB} post       100000 --user=admin --password=mypwds
    mysql_random_data_load ${MYSQL_DB} foot_stamp 100000 --user=admin --password=mypwds

run-mysql-db: lunch-mysql-db check-mysql-db setup-mysql-db inject-mysql-db

clean-mysql-db:
    #!/bin/bash
    if [ {{ MYSQL_DOCKER_EXISTS_FLAG }} = 0 ]; then
        echo "{{ MYSQL_DOCKER }} is not exists. Not work."
    else
        docker kill {{ MYSQL_DOCKER }} > /dev/null && echo 'clean up the mysql docker'
    fi
