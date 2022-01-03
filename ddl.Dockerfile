FROM debian:11.1-slim

# mysql versionを固定
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

RUN mysql --version

RUN mkdir /app
WORKDIR /app
COPY src/main/resources/data/fulldb-11-12-2021-15-57-beta.sql .

CMD ["/bin/sh", "-c", "mysql -uadmin -pmypwds -hmoco-moco-cluster-01-primary.moco-cluster.svc.cluster.local < /app/fulldb-11-12-2021-15-57-beta.sql"]