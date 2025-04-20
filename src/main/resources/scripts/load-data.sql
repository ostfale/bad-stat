create table bad_stat_config
(
    id                         bigint not null,
    databaseCW                 varchar(255),
    lastRankingFileDownload    timestamp(6),
    lastTournamentFileDownload timestamp(6),
    rankingFileName            varchar(255),
    tournamentFileName         varchar(255),
    primary key (id)
);

create table PLAYER
(
    id                 bigint not null,
    ageClassDetail     varchar(255),
    ageClassGeneral    varchar(255),
    clubName           varchar(255),
    createdAt          timestamp(6) not null,
    districtName       varchar(255),
    doubleAgeRanking   integer,
    doublePoints       integer,
    doubleRanking      integer,
    doubleTournaments  integer,
    favorite           boolean,
    firstName          varchar(255),
    fullName           varchar(255),
    gender             enum ('FEMALE','MALE'),
    lastName           varchar(255),
    mixedAgeRanking    integer,
    mixedPoints        integer,
    mixedRanking       integer,
    mixedTournaments   integer,
    playerId           varchar(255),
    playerTournamentId varchar(255),
    singleAgeRanking   integer,
    singlePoints       integer,
    singleRanking      integer,
    singleTournaments  integer,
    stateGroup         enum ('CENTER','NORTH','NO_GROUP','SOUTH_EAST','WEST'),
    stateName          varchar(255),
    updatedAt          timestamp(6) not null,
    yearOfBirth        integer,
    primary key (id)
);

create table public.Match
(
    id                   bigint  not null,
    discipline           enum ('DOUBLE','MIXED','SINGLE'),
    disciplineName       varchar(255),
    matchDuration        varchar(255),
    matchOrder           integer not null,
    matchResult          varchar(255),
    roundName            varchar(255),
    teamOnePlayerOneName varchar(255),
    teamOnePlayerTwoName varchar(255),
    teamTwoPlayerOneName varchar(255),
    teamTwoPlayerTwoName varchar(255),
    tournament_id        bigint  not null,
    primary key (id)
);



create table public.Tournament
(
    id                  bigint not null,
    tournamentDate      varchar(255),
    tournamentID        varchar(255),
    tournamentLocation  varchar(255),
    tournamentName      varchar(255),
    tournamentOrganizer varchar(255),
    tournamentYear      integer,
    primary key (id)
);

create table public.TournamentsStatistic
(
    id                                  bigint not null,
    playerId                            varchar(255),
    yearDownloadedTournaments           integer,
    yearMinusOneDownloadedTournaments   integer,
    yearMinusOnePlayedTournaments       integer,
    yearMinusThreeDownloadedTournaments integer,
    yearMinusThreePlayedTournaments     integer,
    yearMinusTwoDownloadedTournaments   integer,
    yearMinusTwoPlayedTournaments       integer,
    yearPlayedTournaments               integer,
    primary key (id)
);

create sequence bad_stat_config_SEQ start with 1 increment by 50;

create sequence Match_SEQ start with 1 increment by 50;

create sequence Player_SEQ start with 1 increment by 50;

create sequence Tournament_SEQ start with 1 increment by 50;

create sequence TournamentsStatistic_SEQ start with 1 increment by 50;

alter table if exists Match
    add constraint FKcsagjfh7xemndxk01bsecnsrv
    foreign key (tournament_id)
    references Tournament;
