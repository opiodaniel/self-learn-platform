create table if not exists course_category (
    id serial primary key,
    name varchar(255)
);

create table if not exists courses (
    id serial primary key,
    description text,
    image_url varchar(255),
    title varchar(255),
    category_id integer references course_category(id)
);