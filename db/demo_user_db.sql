-- auto-generated definition
create table tf_sys_user
(
    user_id        bigint auto_increment comment '用户标识'
        primary key,
    user_name      varchar(80)          null comment '用户名称',
    staff_id       varchar(30)          null comment '账号',
    sex            char                 null comment '性别：M-男，W-女',
    mobile_no      varchar(100)         null comment '手机号码',
    provinces_code int                  null comment '省编码',
    city_code      int                  null comment '城市编码',
    area_code      int                  null comment '地区编码',
    address        varchar(80)          null comment '详细地址',
    remark         varchar(300)         null comment '备注',
    valid_tag      tinyint(1) default 1 not null comment '有效状态：1-有效，0-无效',
    create_by      varchar(20)          null comment '创建人',
    create_time    datetime             null comment '创建时间',
    update_by      varchar(20)          null comment '修改人',
    update_time    datetime             null comment '修改时间',
    constraint tf_sys_user_mobile_no_uindex
        unique (mobile_no),
    constraint tf_sys_user_staff_id_uindex
        unique (staff_id)
)
    comment '用户资料表';
