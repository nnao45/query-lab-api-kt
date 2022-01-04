FROM debian:11.1-slim

ARG USER=admin
ARG PASSWORD=mypwds
ARG HOST=moco-moco-cluster-01-primary.moco-cluster.svc.cluster.local
ARG MYSQL_DB=jq_api
ARG MYSQL_VERSION=8.0.25

RUN set -eux; \
    apt-get update && \
    apt-get install -y --no-install-recommends \
        ca-certificates \
        wget \
        # MySQL系
        mysql-common \
        libatomic1 \
        libaio1 \
        libncurses6 \
        libnuma1 \
        libsasl2-2

# mysql clientのインストール
RUN mkdir /tmp/lib
WORKDIR  /tmp/lib

RUN wget https://github.com/Percona-Lab/mysql_random_data_load/releases/download/v0.1.12/mysql_random_data_load_0.1.12_Linux_x86_64.tar.gz
RUN tar xvfz mysql_random_data_load_0.1.12_Linux_x86_64.tar.gz
RUN chmod a+x mysql_random_data_load
RUN mv mysql_random_data_load /bin/

RUN wget https://dev.mysql.com/get/Downloads/MySQL-8.0/mysql-common_$MYSQL_VERSION-1debian10_amd64.deb \
    https://dev.mysql.com/get/Downloads/MySQL-8.0/libmysqlclient21_$MYSQL_VERSION-1debian10_amd64.deb \
    https://dev.mysql.com/get/Downloads/MySQL-8.0/mysql-community-client-core_$MYSQL_VERSION-1debian10_amd64.deb \
    https://dev.mysql.com/get/Downloads/MySQL-8.0/mysql-community-client_$MYSQL_VERSION-1debian10_amd64.deb \
    https://dev.mysql.com/get/Downloads/MySQL-8.0/libmysqlclient-dev_$MYSQL_VERSION-1debian10_amd64.deb \
    https://cdn.mysql.com//archives/mysql-8.0/mysql-community-client-plugins_$MYSQL_VERSION-1debian10_amd64.deb

RUN dpkg -i \
    mysql-common_$MYSQL_VERSION-1debian10_amd64.deb \
    libmysqlclient21_$MYSQL_VERSION-1debian10_amd64.deb \
    mysql-community-client-core_$MYSQL_VERSION-1debian10_amd64.deb \
    mysql-community-client_$MYSQL_VERSION-1debian10_amd64.deb \
    libmysqlclient-dev_$MYSQL_VERSION-1debian10_amd64.deb \
    mysql-community-client-plugins_$MYSQL_VERSION-1debian10_amd64.deb

RUN rm -rf /tmp/lib

# 動くか確認をする
RUN mysql --version
RUN mysql_random_data_load --version test test 1

CMD echo "CREATE DATABASE IF NOT EXISTS ${MYSQL_DB} DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci" | mysql -h${HOST} -umoco-admin && \
    echo 'CREATE USER '\'admin\''@'\'%\'' IDENTIFIED BY '\'${PASSWORD}\''' | mysql -h${HOST} -umoco-admin && \
    echo 'GRANT ALL On *.* To admin@'\'%\'';' | mysql -h${HOST} -umoco-admin && \
    echo 'set global time_zone = "Asia/Tokyo"' | mysql -h${HOST} -umoco-admin && \
    mysql_random_data_load ${MYSQL_DB} user       100000 --user=${USER} --password=${PASSWORD} --host=${HOST} && \
    mysql_random_data_load ${MYSQL_DB} post       100000 --user=${USER} --password=${PASSWORD} --host=${HOST} && \
    mysql_random_data_load ${MYSQL_DB} foot_stamp 100000 --user=${USER} --password=${PASSWORD} --host=${HOST}