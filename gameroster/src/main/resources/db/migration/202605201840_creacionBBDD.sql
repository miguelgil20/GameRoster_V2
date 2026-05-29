CREATE TABLE favorite
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    user_id    BIGINT   NOT NULL,
    team_id    BIGINT   NOT NULL,
    created_at datetime NOT NULL,
    CONSTRAINT pk_favorite PRIMARY KEY (id)
);

CREATE TABLE players
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    nickname   VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    `role`     VARCHAR(255) NULL,
    country    VARCHAR(255) NULL,
    created_at datetime     NOT NULL,
    team_id    BIGINT       NOT NULL,
    CONSTRAINT pk_players PRIMARY KEY (id)
);

CREATE TABLE `role`
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE teams
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255) NOT NULL,
    region     VARCHAR(255) NULL,
    ranking    INT NULL,
    logo_url   VARCHAR(255) NULL,
    created_at datetime     NOT NULL,
    CONSTRAINT pk_teams PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    role_id     BIGINT NOT NULL,
    user_codigo BIGINT NOT NULL
);

CREATE TABLE usuario
(
    code          BIGINT AUTO_INCREMENT NOT NULL,
    username      VARCHAR(255) NOT NULL,
    password      VARCHAR(255) NOT NULL,
    creation_date datetime     NOT NULL,
    email         VARCHAR(255) NOT NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (code)
);

ALTER TABLE usuario
    ADD CONSTRAINT uc_usuario_email UNIQUE (email);

ALTER TABLE usuario
    ADD CONSTRAINT uc_usuario_username UNIQUE (username);

ALTER TABLE favorite
    ADD CONSTRAINT FK_FAVORITE_ON_TEAM FOREIGN KEY (team_id) REFERENCES teams (id);

ALTER TABLE favorite
    ADD CONSTRAINT FK_FAVORITE_ON_USER FOREIGN KEY (user_id) REFERENCES usuario (code);

ALTER TABLE players
    ADD CONSTRAINT FK_PLAYERS_ON_TEAM FOREIGN KEY (team_id) REFERENCES teams (id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_role FOREIGN KEY (role_id) REFERENCES `role` (id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_user FOREIGN KEY (user_codigo) REFERENCES usuario (code);