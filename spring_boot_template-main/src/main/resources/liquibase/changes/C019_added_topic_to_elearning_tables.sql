create table if not exists course_topic (
    id serial primary key,
    description text,
    title varchar(255),
    course_id integer references courses(id)
);

