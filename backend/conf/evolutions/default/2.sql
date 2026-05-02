# --- !Ups
ALTER TABLE researcher_info ADD COLUMN author_id BIGINT;

# --- !Downs
ALTER TABLE researcher_info DROP COLUMN author_id;
