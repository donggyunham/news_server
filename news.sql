-- news schema create
create database news;

-- news schema use
use news;

-- category, source, article table create
-- category table : id(auto_increment), name(varchar), memo(varchar), created_at, updated_at

create table category(
`id` bigint not null auto_increment primary key,
`name` varchar(50) not null,
`memo` varchar(500),
`created_at` timestamp not null default current_timestamp,
`updated_at` timestamp not null default current_timestamp on update current_timestamp
);

drop table if exists category;

insert into category(`name`) values('general');

-- table name : source
-- id: bigint(auto_increment)
-- "sid": "abc-news",
-- "name": "ABC News",
-- "description": "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.",
-- "url": "https://abcnews.go.com",
-- "category": "general",
-- "language": "en",
-- "country": "us"
-- created_at, updated_at
/*
alter table `source`
add constraint uq_source_name unique(`name`); -- name coulmn unique 속성 추가
*/
create table source(
	`id` bigint not null auto_increment primary key,
    `sid` varchar(100),
    `name` varchar(100) unique,
    `description` varchar(1000),
    `url` varchar(1000),
    `category` varchar(50),
    `language` varchar(50),
    `country` varchar(10),
    `created_at`timestamp not null default current_timestamp,
    `updated_at`timestamp not null default current_timestamp on update current_timestamp
);

-- article
-- id : bigint auto_increment
-- "source": bigint
-- category : bigint
-- "author": varchar(50)
-- "title": varchar(200)
-- "description": text
-- "url": varchar(1000)
-- "urlToImage": varchar(1000)
-- "publishedAt": varchar(100)
-- "content": text
-- created_at
-- updated_at

-- drop table if exists article;

create table article(
	`id` bigint not null auto_increment primary key,
    `source_id` bigint,
    `category_id` bigint,
    `author` varchar(150),
    `title` varchar(500),
    `description` text,
    `url` varchar(500) unique,
    `url_to_image` varchar(500),
    `published_at` varchar(100),
    `content` text,
    `created_at` timestamp not null default current_timestamp,
    `updated_at` timestamp not null default current_timestamp on update current_timestamp,
    constraint foreign key(`source_id`) references `source`(`id`),
    constraint foreign key(`category_id`) references `category`(`id`)
);

-- alter table : 테이블의 속성을 수정, 보완하는 명령
alter table article add constraint foreign key(`source_id`) references `source`(`id`);
alter table article add constraint foreign key(`category_id`) references `category`(`id`);

/*
alter table aritcle rename to article;
*/

-- drop table if exists article;

-- update category
-- set id = '7'
-- where id = '8';

use news;

show create table article;
show create table category;
show create table source;

select * from article;
select * from category;
select * from source;

-- update category set name = 'entertainment' where id = 4;