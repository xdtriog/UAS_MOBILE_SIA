/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     12/20/2024 9:54:04 PM                        */
/*==============================================================*/


drop table if exists BUKU_BESAR;

drop table if exists CUSTOMER;

drop table if exists DETAIL_JURNAL_UMUM;

drop table if exists DETAIL_NOTA_JUAL;

drop table if exists JURNAL_UMUM;

drop table if exists KARYAWAN;

drop table if exists KODE_AKUN;

drop table if exists MASTER_BARANG;

drop table if exists MASTER_MEMBERSHIP;

drop table if exists MASTER_METODE_PEMBAYARAN;

drop table if exists NOTA_JUAL;

drop table if exists PIUTANG_CUSTOMER;

drop table if exists TRANS_CICILAN_PIUTANG;

drop table if exists TRANS_MEMBERSHIP;

/*==============================================================*/
/* Table: BUKU_BESAR                                            */
/*==============================================================*/
create table BUKU_BESAR
(
   ID_BB                char(10) not null,
   ID_KODE_AKUN         char(10),
   REF                  char(10),
   DEBIT                char(10),
   KREDIT               char(10),
   SALDO_DEBIT          char(10),
   SALDO_KREDIT         char(10),
   primary key (ID_BB)
);

/*==============================================================*/
/* Table: CUSTOMER                                              */
/*==============================================================*/
create table CUSTOMER
(
   NIK_CUTOMER          numeric(16,0) not null,
   NAMA_CUSTOMER        varchar(50),
   EMAIL                varchar(100),
   primary key (NIK_CUTOMER)
);

/*==============================================================*/
/* Table: DETAIL_JURNAL_UMUM                                    */
/*==============================================================*/
create table DETAIL_JURNAL_UMUM
(
   ID_DETAIL_JU         varchar(25) not null,
   ID_JU                varchar(20),
   TANGGAL              timestamp,
   REF                  varchar(20),
   DEBIT                numeric(25,0),
   KREDIT               numeric(25,0),
   primary key (ID_DETAIL_JU)
);

/*==============================================================*/
/* Table: DETAIL_NOTA_JUAL                                      */
/*==============================================================*/
create table DETAIL_NOTA_JUAL
(
   ID_DETAIL_NOTA_JUAL  varchar(25) not null,
   NOMOR_NOTA           varchar(20),
   KD_BARANG            varchar(10),
   JUMLAH               numeric(5,0),
   TOTAL_HARGA          numeric(25,0),
   primary key (ID_DETAIL_NOTA_JUAL)
);

/*==============================================================*/
/* Table: JURNAL_UMUM                                           */
/*==============================================================*/
create table JURNAL_UMUM
(
   ID_JU                varchar(20) not null,
   ID_KODE_AKUN         varchar(10),
   PERIODE              numeric(6,0),
   primary key (ID_JU)
);

/*==============================================================*/
/* Table: KARYAWAN                                              */
/*==============================================================*/
create table KARYAWAN
(
   NIK_KARYAWAN         numeric(16,0) not null,
   NAMA_KARYAWAN        varchar(50),
   EMAIL                varchar(100),
   PASSWORD             varchar(50),
   STATUS               numeric(1,0),
   primary key (NIK_KARYAWAN)
);

/*==============================================================*/
/* Table: KODE_AKUN                                             */
/*==============================================================*/
create table KODE_AKUN
(
   ID_KODE_AKUN         varchar(10) not null,
   KODE_AKUN            numeric(4,0),
   NAMA_AKUN            varchar(100),
   primary key (ID_KODE_AKUN)
);

/*==============================================================*/
/* Table: MASTER_BARANG                                         */
/*==============================================================*/
create table MASTER_BARANG
(
   KD_BARANG            varchar(10) not null,
   NAMA_BARANG          varchar(100),
   HARGA_BARANG         numeric(25,0),
   STATUS               numeric(1,0),
   primary key (KD_BARANG)
);

/*==============================================================*/
/* Table: MASTER_MEMBERSHIP                                     */
/*==============================================================*/
create table MASTER_MEMBERSHIP
(
   ID_MASTER_MEMBERSHIP varchar(10) not null,
   NIK_KARYAWAN         numeric(16,0),
   NAMA_MEMBERSHIP      varchar(100),
   HARGA_MEMBERSHIP     numeric(25,0),
   POTONGAN_            numeric(2,0),
   STATUS               numeric(1,0),
   LAST_UPDATED         timestamp not null,
   primary key (ID_MASTER_MEMBERSHIP)
);

/*==============================================================*/
/* Table: MASTER_METODE_PEMBAYARAN                              */
/*==============================================================*/
create table MASTER_METODE_PEMBAYARAN
(
   ID_MMPN              varchar(10) not null,
   NAMA_MPPN            varchar(100),
   STATUS               numeric(1,0),
   primary key (ID_MMPN)
);

/*==============================================================*/
/* Table: NOTA_JUAL                                             */
/*==============================================================*/
create table NOTA_JUAL
(
   NOMOR_NOTA           varchar(20) not null,
   NIK_CUTOMER          numeric(16,0),
   NIK_KARYAWAN         numeric(16,0),
   ID_MPP               varchar(10),
   TIMESTAMP            timestamp,
   GRAND_TOTAL          numeric(25,0),
   primary key (NOMOR_NOTA)
);

/*==============================================================*/
/* Table: PIUTANG_CUSTOMER                                      */
/*==============================================================*/
create table PIUTANG_CUSTOMER
(
   ID_PIUTANG_CUSTOMER  varchar(20) not null,
   NOMOR_NOTA           varchar(20),
   MAX_TEMPO            timestamp,
   CICILAN              numeric(2,0),
   JUMLAH_PIUTANG       numeric(25,0),
   primary key (ID_PIUTANG_CUSTOMER)
);

/*==============================================================*/
/* Table: TRANS_CICILAN_PIUTANG                                 */
/*==============================================================*/
create table TRANS_CICILAN_PIUTANG
(
   ID_CICILAN_PIUTANG   varchar(25) not null,
   ID_PIUTANG_CUSTOMER  varchar(20),
   JUMLAH_BAYAR         numeric(25,0),
   WAKTU_CICIL          timestamp,
   primary key (ID_CICILAN_PIUTANG)
);

/*==============================================================*/
/* Table: TRANS_MEMBERSHIP                                      */
/*==============================================================*/
create table TRANS_MEMBERSHIP
(
   ID_TRANS_MEMBERSHIP  varchar(20) not null,
   ID_MASTER_MEMBERSHIP varchar(10),
   NIK_CUTOMER          numeric(16,0),
   NAMA_MEMBERSHIP      varchar(100),
   HARGA_MEMBERSHIP     numeric(25,0),
   POTONGAN_            numeric(2,0),
   TIMESTAMP_HABIS      timestamp,
   primary key (ID_TRANS_MEMBERSHIP)
);

alter table BUKU_BESAR add constraint FK_REFERENCE_24 foreign key (ID_KODE_AKUN)
      references KODE_AKUN (ID_KODE_AKUN) on delete restrict on update restrict;

alter table DETAIL_JURNAL_UMUM add constraint FK_REFERENCE_23 foreign key (ID_JU)
      references JURNAL_UMUM (ID_JU) on delete restrict on update restrict;

alter table DETAIL_NOTA_JUAL add constraint FK_REFERENCE_20 foreign key (NOMOR_NOTA)
      references NOTA_JUAL (NOMOR_NOTA) on delete restrict on update restrict;

alter table DETAIL_NOTA_JUAL add constraint FK_REFERENCE_21 foreign key (KD_BARANG)
      references MASTER_BARANG (KD_BARANG) on delete restrict on update restrict;

alter table JURNAL_UMUM add constraint FK_REFERENCE_22 foreign key (ID_KODE_AKUN)
      references KODE_AKUN (ID_KODE_AKUN) on delete restrict on update restrict;

alter table MASTER_MEMBERSHIP add constraint FK_REFERENCE_16 foreign key (NIK_KARYAWAN)
      references KARYAWAN (NIK_KARYAWAN) on delete restrict on update restrict;

alter table NOTA_JUAL add constraint FK_REFERENCE_12 foreign key (NIK_CUTOMER)
      references CUSTOMER (NIK_CUTOMER) on delete restrict on update restrict;

alter table NOTA_JUAL add constraint FK_REFERENCE_13 foreign key (NIK_KARYAWAN)
      references KARYAWAN (NIK_KARYAWAN) on delete restrict on update restrict;

alter table NOTA_JUAL add constraint FK_REFERENCE_14 foreign key (ID_MPP)
      references MASTER_METODE_PEMBAYARAN (ID_MMPN) on delete restrict on update restrict;

alter table PIUTANG_CUSTOMER add constraint FK_REFERENCE_19 foreign key (NOMOR_NOTA)
      references NOTA_JUAL (NOMOR_NOTA) on delete restrict on update restrict;

alter table TRANS_CICILAN_PIUTANG add constraint FK_REFERENCE_15 foreign key (ID_PIUTANG_CUSTOMER)
      references PIUTANG_CUSTOMER (ID_PIUTANG_CUSTOMER) on delete restrict on update restrict;

alter table TRANS_MEMBERSHIP add constraint FK_REFERENCE_17 foreign key (ID_MASTER_MEMBERSHIP)
      references MASTER_MEMBERSHIP (ID_MASTER_MEMBERSHIP) on delete restrict on update restrict;

alter table TRANS_MEMBERSHIP add constraint FK_REFERENCE_18 foreign key (NIK_CUTOMER)
      references CUSTOMER (NIK_CUTOMER) on delete restrict on update restrict;

