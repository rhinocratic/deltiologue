--;;
create extension moddatetime;
--;;
create table if not exists content (
    id int generated always as identity,
    draft boolean not null default true,
    title text not null,
    content text not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp,
    primary key (id)
);
--;;
create trigger content_moddatetime
	before update on content
	for each row
	execute procedure moddatetime (updated_at);
--;;
create table if not exists publisher (
    id int generated always as identity,
    publisher_name text not null,
    primary key (id),
    unique(publisher_name)
);
--;;
create table if not exists tag_category (
    id int generated always as identity,
    category_name text not null,
    display_text text not null,
    colour text not null,
    primary key(id),
    unique(category_name),
    constraint rgb_colour check (colour ~* '^[a-f0-9]{6}$')
);
--;;
create index idx_tag_category_category_name on tag_category(category_name);
--;;
create table if not exists tag (
    id int generated always as identity,
    tag_name text not null,
    display_text text not null,
    primary key(id),
    unique(tag_name)
);
--;;
create table if not exists stamp (
    id int generated always as identity,
    stamp_description text not null,
    primary key(id),
    unique(stamp_description)
);
--;;
create table if not exists series (
    id int generated always as identity,
    publisher_id int,
    series_name text not null,
    primary key(id),
    unique(series_name),
    constraint fk_series_publisher foreign key(publisher_id) references publisher(id) on delete cascade
);
--;;
create table if not exists note_image (
    id int generated always as identity,
    filename text not null,
    alt_text text not null,
    caption text not null,
    primary key(id)
);
--;;
create table if not exists postcard (
    id int generated always as identity,
    draft boolean not null default true,
    index int not null,
    divided_back boolean not null,
    rp boolean not null,
    used boolean not null,
    posted boolean not null,
    franked boolean not null,
    image_front_alt text,
    image_rear_alt text,
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
    series_id int,
    publisher_id int,
    publication_description text,
    recipient_name text,
    recipient_address text,
    recipient_location geography,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp,
    primary key(id),
    unique(index),
    constraint fk_postcard_publisher foreign key(publisher_id) references publisher(id) on delete cascade,
    constraint fk_postcard_series foreign key(series_id) references series(id) on delete cascade
);
--;;
alter table postcard add column fts tsvector
generated always as
(setweight(to_tsvector('english', coalesce(subject_description, '')), 'A') ||
 setweight(to_tsvector('english', coalesce(notes, '')), 'B')) STORED;
--;;
create index idx_postcard_fts_gin on postcard using gin (fts);
--;;
create index idx_postcard_subject_location on postcard using gist (geography(subject_location));
--;;
create index idx_postcard_recipient_location on postcard using gist (geography(recipient_location));
--;;
create trigger postcard_moddatetime
	before update on postcard
	for each row
	execute procedure moddatetime (updated_at);
--;;
create table if not exists slideshow (
    id int generated always as identity,
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
    id int generated always as identity,
    draft boolean not null default true,
    title text,
    body text,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp,
    primary key(id)
);
--;;
create trigger note_moddatetime
	before update on note
	for each row
	execute procedure moddatetime (updated_at);
--;;
create table if not exists reference (
    id int generated always as identity,
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