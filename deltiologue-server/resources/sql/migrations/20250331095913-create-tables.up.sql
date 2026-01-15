create table if not exists publisher (
    id int generated always as identity (minvalue 0 start with 0 increment by 1),
    publisher_name text not null,
    primary key (id),
    unique(publisher_name)
);
--;;
create table if not exists tag_category (
    id int generated always as identity (minvalue 0 start with 0 increment by 1),
    category_name text not null,
    display_text text not null,
    primary key(id),
    unique(category_name)
);
--;;
create index idx_tag_category_category_name on tag_category(category_name);
--;;
create table if not exists tag (
    id int generated always as identity (minvalue 0 start with 0 increment by 1),
    tag_name text not null,
    display_text text not null,
    primary key(id),
    unique(tag_name)
);
--;;
create table if not exists stamp (
    id int generated always as identity (minvalue 0 start with 0 increment by 1),
    stamp_description text not null,
    primary key(id),
    unique(stamp_description)
);
--;;
create table if not exists series (
    id int generated always as identity (minvalue 0 start with 0 increment by 1),
    series_name text not null,
    primary key(id),
    unique(series_name)
);
--;;
create table if not exists recipient (
    id int generated always as identity (minvalue 0 start with 0 increment by 1),
    recipient_name text not null,
    recipient_address text,
    recipient_location geometry,
    primary key(id),
    unique(recipient_name)
);
--;;
create index idx_recipient_location on recipient using gist (geography(recipient_location));
--;;
create table if not exists image (
    id int generated always as identity (minvalue 0 start with 0 increment by 1),
    url text not null,
    alt_text text not null,
    title text not null,
    primary key(id),
    unique(url)
);
--;;
create table if not exists note_image (
    id int generated always as identity (minvalue 0 start with 0 increment by 1),
    filename text not null,
    alt_text text not null,
    caption text not null,
    primary key(id)
);
--;;
create table if not exists postcard (
    id int generated always as identity (minvalue 0 start with 0 increment by 1),
    collection_index int not null,
    divided_back boolean not null,
    rp boolean not null,
    used boolean not null,
    posted boolean not null,
    franked boolean not null,
    image_front int,
    image_front_alt text,
    image_rear int,
    image_rear_alt text,
    image_thumb int,
    publication_year int,
    publication_month int,
    publication_day int,
    publication_date date,
    publication_date_approximate boolean not null,
    posted_year int,
    posted_month int,
    posted_day int,
    posted_date date,
    posted_date_approximate boolean not null,
    subject_description text,
    subject_location geography,
    subject_current_view text,
    notes text,
    transcript text,
    publisher int,
    recipient int,
    series int,
    series_entry text,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp,
    primary key(id),
    unique(collection_index),
    constraint fk_postcard_image_front foreign key(image_front) references image(id) on delete cascade,
    constraint fk_postcard_image_rear foreign key(image_rear) references image(id) on delete cascade,
    constraint fk_postcard_image_thumb foreign key(image_thumb) references image(id) on delete cascade,
    constraint fk_postcard_publsher foreign key(publisher) references publisher(id) on delete cascade,
    constraint fk_postcard_recipient foreign key(recipient) references recipient(id) on delete cascade,
    constraint fk_postcard_series foreign key(series) references series(id) on delete cascade
);
--;;
alter table postcard add column fts tsvector
generated always as
(setweight(to_tsvector('english', coalesce(subject_description, '')), 'A') ||
 setweight(to_tsvector('english', coalesce(notes, '')), 'B')) STORED;
--;;
create index idx_postcard_fts_gin on postcard using gin (fts);
--;;
create index idx_postcard_location on postcard using gist (geography(subject_location));
--;;
create table if not exists slideshow (
    id int generated always as identity (minvalue 0 start with 0 increment by 1),
    postcard_id int not null,
    constraint fk_slideshow_postcard foreign key(postcard_id) references postcard(id) on delete cascade
);
--;;
create type stamp_condition as enum('intact', 'partially removed', 'damaged', 'badly damaged');
--;;
create table if not exists postcard_stamp (
    postcard_id int not null,
    stamp_id int,
    stamp_condition stamp_condition not null,
    constraint fk_postcard_stamp_postcard_id foreign key(postcard_id) references postcard(id) on delete cascade,
    constraint fk_postcard_stamp_stamp_id foreign key(stamp_id) references stamp(id) on delete cascade
);
--;;
create index idx_postcard_stamp_postcard on postcard_stamp(postcard_id);
--;;
create index idx_postcard_stamp_stamp on postcard_stamp(stamp_id);
--;;
create table if not exists postcard_tag (
    postcard_id int not null,
    tag_id int not null,
    tag_category_id int not null,
    unique(postcard_id, tag_id, tag_category_id),
    constraint fk_postcard_tag_postcard_id foreign key(postcard_id) references postcard(id) on delete cascade,
    constraint fk_postcard_tag_tag_id foreign key(tag_id) references tag(id) on delete cascade,
    constraint fk_postcard_tag_tag_category_id foreign key(tag_category_id) references tag_category(id) on delete cascade
);
--;;
create index idx_postcard_tag_postcard on postcard_tag(postcard_id);
--;;
create index idx_postcard_tag_tag on postcard_tag(tag_id);
--;;
create index idx_postcard_tag_tag_category on postcard_tag(tag_category_id);
--;;
create table if not exists note (
    id int generated always as identity (minvalue 0 start with 0 increment by 1),
    title text,
    body text,
    primary key(id)
);
--;;
create table if not exists reference (
    id int generated always as identity (minvalue 0 start with 0 increment by 1),
    idx int not null,
    medium text,
    accessed date,
    source text,
    title text,
    issue_date date,
    issue_note text,
    available text,
    primary key(id),
    unique(idx)
);
--;;
create index idx_reference_idx on reference(idx);
--;;
create table if not exists note_reference (
    note_id int not null,
    reference_id int not null,
    unique(note_id, reference_id),
    constraint fk_note_reference_note_id foreign key(note_id) references note(id) on delete cascade,
    constraint fk_note_reference_reference_id foreign key(reference_id) references reference(id) on delete cascade
);
--;;
create index idx_note_reference_note on note_reference(note_id);
--;;
create index idx_note_reference_reference on note_reference(reference_id);
--;;
--;;
create or replace function card_tags(card_id int)
returns table (tag_name text, display_text text, tag_category text)
as
$$
select t.tag_name, t.display_text, c.display_text
from postcard_tag pt
left join tag t on pt.tag_id = t.id
left join tag_category c on pt.tag_category_id = c.id
where pt.postcard_id = card_id;
$$
language 'sql' stable parallel safe;
--;;