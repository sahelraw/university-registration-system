-- 1. Fix the Enrollments table using the EXACT constraint name from your error
ALTER TABLE enrollments 
DROP CONSTRAINT "fkm6ptklbuk36d0q5nb8vpwnj48"; 

ALTER TABLE enrollments 
ADD CONSTRAINT fk_enrollments_course
FOREIGN KEY (course_id) 
REFERENCES course(id) 
ON DELETE CASCADE;

-- 2. Fix the Sections table
-- Since the name for sections is likely also random, let's find it.
-- If the line below fails, check the "Constraints" folder under the sections table in pgAdmin.
ALTER TABLE sections 
DROP CONSTRAINT IF EXISTS fk_sections_course; -- Try a generic drop first

ALTER TABLE sections 
ADD CONSTRAINT fk_sections_course
FOREIGN KEY (course_id) 
REFERENCES course(id) 
ON DELETE CASCADE;