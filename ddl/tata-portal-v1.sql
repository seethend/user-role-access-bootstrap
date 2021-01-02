
    create table cmps_address_details (
       addr_id integer not null auto_increment,
        city varchar(500) not null,
        country varchar(500) not null,
        create_date date not null,
        state varchar(500) not null,
        street varchar(500) not null,
        update_date date,
        zip_code varchar(500),
        primary key (addr_id)
    ) engine=InnoDB

    create table cmps_app_modules (
       module_id integer not null auto_increment,
        module_name varchar(255),
        parent_module_id integer,
        primary key (module_id)
    ) engine=InnoDB

    create table cmps_role_access_mappings (
       mapping_id integer not null auto_increment,
        module_id integer,
        role_id integer,
        primary key (mapping_id)
    ) engine=InnoDB

    create table cmps_roles (
       role_id integer not null auto_increment,
        role_name varchar(255),
        primary key (role_id)
    ) engine=InnoDB

    create table cmps_user (
       user_id integer not null auto_increment,
        active integer not null,
        avatar TEXT NULL,
        create_date date not null,
        date_of_birth date,
        first_name varchar(250) not null,
        gender varchar(100),
        hire_date date,
        last_name varchar(250),
        linkedin varchar(250),
        marital_status varchar(100),
        marketing_email_id varchar(250),
        middle_name varchar(250),
        password varchar(250) not null,
        personal_email_id varchar(250),
        phone_number varchar(10),
        ssn_or_pan varchar(250),
        update_date date,
        username varchar(250) not null,
        visa_status varchar(100),
        addr_id integer,
        primary key (user_id)
    ) engine=InnoDB

    create table cmps_user_roles (
       user_id integer not null,
        role_id integer not null,
        primary key (user_id, role_id)
    ) engine=InnoDB

    alter table cmps_user 
       add constraint UK_l8rjwh9a6ecaooos70vorn01p unique (username)

    alter table cmps_user_roles 
       add constraint UK_6b0lrsysy2b822n4l7eucu3w8 unique (role_id)

    alter table cmps_app_modules 
       add constraint FKbxut3ovg3odv28yuni1u3yvae 
       foreign key (parent_module_id) 
       references cmps_app_modules (module_id)

    alter table cmps_role_access_mappings 
       add constraint FKqj8drm9303h90ya5aclweiuer 
       foreign key (module_id) 
       references cmps_app_modules (module_id)

    alter table cmps_role_access_mappings 
       add constraint FKsl5hxgacfg8db2k448gp8wtj6 
       foreign key (role_id) 
       references cmps_roles (role_id)

    alter table cmps_user 
       add constraint FK565cohfqq7krpbw8ln2mwnx9s 
       foreign key (addr_id) 
       references cmps_address_details (addr_id)

    alter table cmps_user_roles 
       add constraint FKqpvf522pu8ni58u191vc37pbb 
       foreign key (role_id) 
       references cmps_roles (role_id)

    alter table cmps_user_roles 
       add constraint FKc90ko7xt7p0sotyuutfjew46j 
       foreign key (user_id) 
       references cmps_user (user_id)
