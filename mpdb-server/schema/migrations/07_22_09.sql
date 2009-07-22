--adds an timestamp,status column to project_invites
--adds an isActive column to projects
alter table project_invites add column action_timestamp timestamp;
alter table project_invites add column status varchar(32);
alter table projects add column isActive char(1);