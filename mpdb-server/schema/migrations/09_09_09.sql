--Hey, cool date today
CREATE INDEX projectmembers_userId ON project_members USING hash(user_id);
CREATE INDEX projectsamples_projectId ON project_samples USING hash(project_id);