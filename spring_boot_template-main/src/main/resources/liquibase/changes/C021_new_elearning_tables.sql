create table if not exists course_test (
    id serial primary key,
    title varchar(255),
    course_topic_id integer references course_topic(id)
);

create table if not exists course_question (
    id serial primary key,
    content text,
    multiple_choice boolean default false,
    course_test_id integer references course_test(id)
);

create table if not exists course_answer (
    id serial primary key,
    answer_text varchar(255),
    is_correct boolean default false,
    course_question_id integer references course_question(id)
);

create table if not exists test_submission (
    id serial primary key,
    score double precision,
    submitted_at timestamp with time zone default now(),
    course_test_id integer references course_test(id),
    user_id varchar(255) references system_user(servspace_id)
);


create table if not exists course_votes (
    id serial primary key,
    vote_type varchar(255),
    course_id integer references courses(id),
    user_id varchar(255) references system_user(servspace_id)
);

create table if not exists course_comments (
    id serial primary key,
    content text,
    created_at timestamp with time zone default now(),
    course_id integer references courses(id),
    user_id varchar(255) references system_user(servspace_id)
);