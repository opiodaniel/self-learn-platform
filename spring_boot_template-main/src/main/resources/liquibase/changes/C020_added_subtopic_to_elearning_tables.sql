create table if not exists course_subtopic (
   id serial primary key,
   content text,
   title varchar(255),
   topic_id integer references course_topic(id)
);

