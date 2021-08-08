create table BAND
(
    BAND_ID identity not null primary key,
    NAME varchar(255) not null,
    FORMATION_DATE date
);

create table ALBUM
(
    ALBUM_ID identity not null primary key,
    TITLE varchar(255) not null,
    RELEASE_DATE date not null,
    BAND_ID bigint not null,
    foreign key (BAND_ID) references BAND(BAND_ID)
);

create table SONG
(
    SONG_ID identity not null primary key,
    TITLE varchar(255) not null,
    LENGTH_MILLIS bigint not null,
    ALBUM_ID bigint not null,
    foreign key (ALBUM_ID) references ALBUM(ALBUM_ID)
);
