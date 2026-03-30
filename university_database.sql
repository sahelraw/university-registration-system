-- 1. Drop the strict constraint that Postman is citing on the enrollments table
ALTER TABLE enrollments 
DROP CONSTRAINT "fkntqvli5ggsvcasikjg7al1o6t";

-- 2. Add the new constraint with ON DELETE CASCADE
ALTER TABLE enrollments 
ADD CONSTRAINT fk_enrollments_major
FOREIGN KEY (major_id) 
REFERENCES majors(id) 
ON DELETE CASCADE;