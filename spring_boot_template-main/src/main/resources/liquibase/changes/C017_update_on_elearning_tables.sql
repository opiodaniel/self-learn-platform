-- First drop the foreign key constraint (if exists)
ALTER TABLE courses DROP CONSTRAINT IF EXISTS courses_category_id_fkey;

-- Then drop the column that references the category
ALTER TABLE courses DROP COLUMN IF EXISTS category_id;

-- Finally drop the course_category table
DROP TABLE IF EXISTS course_category;


ALTER TABLE courses ADD COLUMN category VARCHAR(50);
ALTER TABLE courses ADD COLUMN published BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE courses ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT now();


