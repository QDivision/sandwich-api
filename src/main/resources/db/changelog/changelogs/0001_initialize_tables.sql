CREATE TABLE sandwiches (
  name       TEXT  NOT NULL PRIMARY KEY,
  bread      JSONB NOT NULL,
  condiments JSONB NOT NULL,
  layers     JSONB NOT NULL
);

