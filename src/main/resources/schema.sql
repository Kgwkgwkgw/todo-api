DROP TABLE IF EXISTS todo;
CREATE TABLE todo
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    desc           varchar(100),
    perform_status VARCHAR(12),
    updated_at     DATETIME
);
CREATE INDEX idx_todo_perform_status_updated_at ON todo (perform_status, updated_at);
CREATE UNIQUE INDEX uk_todo_desc ON todo (desc);
