-- Initial test data for tasks table
-- This data is loaded when the application starts

-- Only insert if the table is empty
INSERT INTO tasks (title, completed, created_at, updated_at)
SELECT * FROM (
    SELECT 'Spring Bootの学習' AS title, false AS completed, CURRENT_TIMESTAMP AS created_at, CURRENT_TIMESTAMP AS updated_at
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM tasks LIMIT 1);
