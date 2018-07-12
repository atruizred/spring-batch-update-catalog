DROP TABLE anime IF EXISTS;

CREATE TABLE anime  (
    anime_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    mal_id VARCHAR(20),
    url VARCHAR(250),
    image_url VARCHAR(250),
    title VARCHAR(100),
    fansub VARCHAR(100),
    description VARCHAR(250),
    type VARCHAR(20),
    score VARCHAR(10),
    episodes VARCHAR(10),
    members VARCHAR(20)
);