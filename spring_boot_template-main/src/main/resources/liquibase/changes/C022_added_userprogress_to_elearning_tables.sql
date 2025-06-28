create table if not exists user_topic_progress_association (
    id serial primary key,
    user_id varchar(255) references system_user(servspace_id),
    course_topic_id integer references course_topic(id),
    progress_percentage double precision default 0,
    completed_at timestamp with time zone default now(),
    completed boolean default false
);

