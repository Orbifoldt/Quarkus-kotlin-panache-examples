create table BAND_UNIDIRECTIONAL
(
    BAND_ID identity not null primary key,
    NAME varchar(255) not null,
    FORMATION_DATE date
);

create table ALBUM_UNIDIRECTIONAL
(
    ALBUM_ID identity not null primary key,
    TITLE varchar(255) not null,
    RELEASE_DATE date not null,
    PARENT_BAND_ID bigint not null,
    foreign key (PARENT_BAND_ID) references BAND_UNIDIRECTIONAL(BAND_ID)
);

create table SONG_UNIDIRECTIONAL
(
    SONG_ID identity not null primary key,
    TITLE varchar(255) not null,
    LENGTH_MILLIS bigint not null,
    PARENT_ALBUM_ID bigint not null,
    foreign key (PARENT_ALBUM_ID) references ALBUM_UNIDIRECTIONAL(ALBUM_ID)
);
