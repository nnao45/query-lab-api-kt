FROM openjdk:11-jre

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

RUN mkdir /tmp/lib
WORKDIR  /tmp/lib

# mysql_random_data_load のインストール
RUN wget https://github.com/Percona-Lab/mysql_random_data_load/releases/download/v0.1.12/mysql_random_data_load_0.1.12_Linux_x86_64.tar.gz
RUN tar xvfz mysql_random_data_load_0.1.12_Linux_x86_64.tar.gz
RUN chmod a+x mysql_random_data_load
RUN mv mysql_random_data_load /bin/

# mysql client のインストール
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

# just のインストール
RUN wget https://github.com/casey/just/releases/download/0.10.5/just-0.10.5-x86_64-unknown-linux-musl.tar.gz
RUN tar xvfz just-0.10.5-x86_64-unknown-linux-musl.tar.gz
RUN chmod a+x just
RUN mv just /bin/

RUN rm -rf /tmp/lib

RUN mkdir /app
WORKDIR /app

# ディレクトリのコピー
COPY . .

RUN mysql --version
RUN mysql_random_data_load --version test test 1
RUN just --version
RUN ./gradlew --version

CMD just setup-mysql-db && just inject-mysql-db