FROM bitnami/minideb:bookworm as builder
ARG EVENTUATE_SCRIPTS_URL=https://ghp.ci/https://raw.githubusercontent.com/eventuate-foundation/eventuate-common/refs/heads/master/postgres
RUN sed -i 's|deb.debian.org|mirrors.tuna.tsinghua.edu.cn|g' /etc/apt/sources.list \
    && apt update \
    && apt install curl -y \
    && curl https://mirrors.tuna.tsinghua.edu.cn/postgresql/repos/apt/ACCC4CF8.asc -o /etc/apt/keyrings/postgres.gpg.asc \
    && echo "deb [ signed-by=/etc/apt/keyrings/postgres.gpg.asc ] https://mirrors.tuna.tsinghua.edu.cn/postgresql/repos/apt/ bookworm-pgdg main 16" > /etc/apt/sources.list.d/pgdg.list \
    && apt update \
    && apt install git make gcc postgresql-server-dev-16 -y \
    && git clone https://ghp.ci/https://github.com/eulerto/wal2json.git \
    && cd wal2json \
    && make \
    && make install \
    && mkdir -p /docker-entrypoint-initdb.d \
    && mkdir -p /additional-scripts \
    && curl $EVENTUATE_SCRIPTS_URL/docker-entrypoint-initdb.d/init-permissions.sh -o /docker-entrypoint-initdb.d/init-permissions.sh \
    && curl $EVENTUATE_SCRIPTS_URL/0.activate-additional-scripts.sh -o /docker-entrypoint-initdb.d/0.activate-additional-scripts.sh \
    && curl $EVENTUATE_SCRIPTS_URL/1.initialize-database.sql -o /docker-entrypoint-initdb.d/1.initialize-database.sql \
    && curl $EVENTUATE_SCRIPTS_URL/2.initialize-database.sql -o /docker-entrypoint-initdb.d/2.initialize-database.sql \
    && curl $EVENTUATE_SCRIPTS_URL/3.initialize-database.sql -o /docker-entrypoint-initdb.d/3.initialize-database.sql \
    && curl $EVENTUATE_SCRIPTS_URL/4.initialize-database-json.sql -o /additional-scripts/4.initialize-database-json.sql \
    && curl $EVENTUATE_SCRIPTS_URL/5.initialize-database-db-id.sql -o /additional-scripts/5.initialize-database-db-id.sql \
    && chown postgres -R /additional-scripts /docker-entrypoint-initdb.d \
    && touch /docker-entrypoint-initdb.d/4.initialize-database-json.sql \
    && touch touch /docker-entrypoint-initdb.d/5.initialize-database-db-id.sql
#    && echo "CREATE SCHEMA IF NOT EXISTS keycloak;" >> /docker-entrypoint-initdb.d/init-keycloak-schema.sql

FROM bitnami/postgresql:16

COPY --from=builder /usr/lib/postgresql/16/lib /opt/bitnami/postgresql/lib
COPY --from=builder /additional-scripts /additional-scripts
COPY --from=builder /docker-entrypoint-initdb.d /docker-entrypoint-initdb.d
# docker:
# POSTGRESQL_EXTRA_FLAGS: "-c wal_level=logical"
# k8s bitnami/postgresql:
#      - primary:
#          extendedConfiguration: |
#            wal_level = logical
# docker-buildx build -t registry.cn-guangzhou.aliyuncs.com/isfong/postgresql:16-wal2json .
# docker push registry.cn-guangzhou.aliyuncs.com/isfong/postgresql:16-wal2json