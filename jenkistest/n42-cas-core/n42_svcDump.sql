--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: action; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE action (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    action character varying(255),
    delaybeforeactioninmins integer,
    actionsforunhealthycanary_canary integer,
    sort_order integer
);


ALTER TABLE public.action OWNER TO postgres;

--
-- Name: bucketscore; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE bucketscore (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    range character varying(255),
    score real
);


ALTER TABLE public.bucketscore OWNER TO postgres;

--
-- Name: canary; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE canary (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    application character varying(255),
    reason character varying(255),
    launcheddate character varying(255),
    id character varying(255),
    canaryname character varying(255),
    lifetimeminutes integer,
    combinedcanaryresultstrategy character varying(255),
    baselineclusterip character varying(255),
    canaryclusterip character varying(255),
    analysisname character varying(255),
    begincanaryanalysisaftermins integer,
    canaryanalysisintervalmins integer,
    lookbackmins integer,
    params character varying(255),
    canaryresultscore character varying(255),
    minimumcanaryresultscore character varying(255),
    chchclass character varying(255),
    canaryclusterid integer,
    baselineclusterid integer,
    status_status integer,
    health_health integer,
    owner character varying(255)
);


ALTER TABLE public.canary OWNER TO postgres;

--
-- Name: canary_notificationhours; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE canary_notificationhours (
    parent_id integer NOT NULL,
    value integer,
    sort_order integer NOT NULL
);


ALTER TABLE public.canary_notificationhours OWNER TO postgres;

--
-- Name: canary_watcherids; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE canary_watcherids (
    parent_id integer NOT NULL,
    value integer,
    sort_order integer NOT NULL
);


ALTER TABLE public.canary_watcherids OWNER TO postgres;

--
-- Name: canaryanalysis; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE canaryanalysis (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    baseversionid integer,
    canaryid integer,
    compareversionid integer,
    finalscore real
);


ALTER TABLE public.canaryanalysis OWNER TO postgres;

--
-- Name: canaryanalysis_metricscoreids; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE canaryanalysis_metricscoreids (
    parent_id integer NOT NULL,
    value integer,
    sort_order integer NOT NULL
);


ALTER TABLE public.canaryanalysis_metricscoreids OWNER TO postgres;

--
-- Name: casservicemetricdetails; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE casservicemetricdetails (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    metricname character varying(255),
    metrictype character varying(255),
    kairosaggregator character varying(255)
);


ALTER TABLE public.casservicemetricdetails OWNER TO postgres;

--
-- Name: casservicemetrics; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE casservicemetrics (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    servicename character varying(255)
);


ALTER TABLE public.casservicemetrics OWNER TO postgres;

--
-- Name: casservicemetrics_metricsdetails; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE casservicemetrics_metricsdetails (
    metricsdetails_casservicemetrics integer NOT NULL,
    casservicemetricdetails integer NOT NULL,
    sort_order integer NOT NULL
);


ALTER TABLE public.casservicemetrics_metricsdetails OWNER TO postgres;

--
-- Name: cluster; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cluster (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    name character varying(255),
    clustertype character varying(255),
    accountname character varying(255),
    region character varying(255),
    buildid character varying(255),
    imageid character varying(255),
    iscanarycluster boolean
);


ALTER TABLE public.cluster OWNER TO postgres;

--
-- Name: health; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE health (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    health character varying(255),
    message character varying(255)
);


ALTER TABLE public.health OWNER TO postgres;

--
-- Name: host; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE host (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    name character varying(255),
    ipaddress character varying(255),
    domainname character varying(255),
    description character varying(255)
);


ALTER TABLE public.host OWNER TO postgres;

--
-- Name: metric; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE metric (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    name character varying(255),
    metrictype character varying(255),
    cummulative boolean,
    aggregator character varying(255)
);


ALTER TABLE public.metric OWNER TO postgres;

--
-- Name: metricscore; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE metricscore (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    metricname character varying(255),
    score real,
    error character varying(255),
    metrictype character varying(255),
    version1stats_versionstats integer,
    version2stats_versionstats integer
);


ALTER TABLE public.metricscore OWNER TO postgres;

--
-- Name: metricscore_bucketscoreids; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE metricscore_bucketscoreids (
    parent_id integer NOT NULL,
    value integer,
    sort_order integer NOT NULL
);


ALTER TABLE public.metricscore_bucketscoreids OWNER TO postgres;

--
-- Name: ownerwatcherdata; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE ownerwatcherdata (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    name character varying(255),
    email character varying(255)
);


ALTER TABLE public.ownerwatcherdata OWNER TO postgres;

--
-- Name: service; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE service (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    name character varying(255),
    displayname character varying(255),
    description character varying(255),
    servicegroup character varying(255)
);


ALTER TABLE public.service OWNER TO postgres;

--
-- Name: service_bucketids; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE service_bucketids (
    parent_id integer NOT NULL,
    value integer,
    sort_order integer NOT NULL
);


ALTER TABLE public.service_bucketids OWNER TO postgres;

--
-- Name: service_hostids; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE service_hostids (
    parent_id integer NOT NULL,
    value integer,
    sort_order integer NOT NULL
);


ALTER TABLE public.service_hostids OWNER TO postgres;

--
-- Name: service_metricids; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE service_metricids (
    parent_id integer NOT NULL,
    value integer,
    sort_order integer NOT NULL
);


ALTER TABLE public.service_metricids OWNER TO postgres;

--
-- Name: serviceversion; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE serviceversion (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    name character varying(255),
    serviceid integer,
    starttime character varying(255),
    endtime character varying(255)
);


ALTER TABLE public.serviceversion OWNER TO postgres;

--
-- Name: serviceversioncharacterstic; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE serviceversioncharacterstic (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    json text
);


ALTER TABLE public.serviceversioncharacterstic OWNER TO postgres;

--
-- Name: serviceversioncharacterstic_serviceversionid; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE serviceversioncharacterstic_serviceversionid (
    parent_id integer NOT NULL,
    value integer,
    sort_order integer NOT NULL
);


ALTER TABLE public.serviceversioncharacterstic_serviceversionid OWNER TO postgres;

--
-- Name: status; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE status (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    status character varying(255),
    complete boolean
);


ALTER TABLE public.status OWNER TO postgres;

--
-- Name: svcresult; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE svcresult (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    svcid character varying(255),
    servicename character varying(255),
    versionfirst character varying(255),
    versionsecond character varying(255),
    result text
);


ALTER TABLE public.svcresult OWNER TO postgres;

--
-- Name: svcservice; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE svcservice (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    servicename character varying(255)
);


ALTER TABLE public.svcservice OWNER TO postgres;

--
-- Name: svcservice_serviceversions; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE svcservice_serviceversions (
    parent_id integer NOT NULL,
    value character varying(255),
    sort_order integer NOT NULL
);


ALTER TABLE public.svcservice_serviceversions OWNER TO postgres;

--
-- Name: svcservicemetricdetails; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE svcservicemetricdetails (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    metricname character varying(255),
    metrictype character varying(255),
    kairosaggregator character varying(255)
);


ALTER TABLE public.svcservicemetricdetails OWNER TO postgres;

--
-- Name: svcservicemetrics; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE svcservicemetrics (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    servicename character varying(255),
    metricname character varying(255)
);


ALTER TABLE public.svcservicemetrics OWNER TO postgres;

--
-- Name: svcversion; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE svcversion (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    versionname character varying(255),
    starttime character varying(255),
    endtime character varying(255),
    hostvalue character varying(255)
);


ALTER TABLE public.svcversion OWNER TO postgres;

--
-- Name: versionstats; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE versionstats (
    n42_id integer NOT NULL,
    type character varying(255) NOT NULL,
    n42createddate timestamp without time zone,
    n42lastupdateddate timestamp without time zone,
    n42hash character varying(255),
    active boolean,
    min real,
    firstqu real,
    median real,
    mean real,
    thirdqu real,
    max real
);


ALTER TABLE public.versionstats OWNER TO postgres;

--
-- Data for Name: action; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY action (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, action, delaybeforeactioninmins, actionsforunhealthycanary_canary, sort_order) FROM stdin;
\.


--
-- Data for Name: bucketscore; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY bucketscore (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, range, score) FROM stdin;
\.


--
-- Data for Name: canary; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY canary (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, application, reason, launcheddate, id, canaryname, lifetimeminutes, combinedcanaryresultstrategy, baselineclusterip, canaryclusterip, analysisname, begincanaryanalysisaftermins, canaryanalysisintervalmins, lookbackmins, params, canaryresultscore, minimumcanaryresultscore, chchclass, canaryclusterid, baselineclusterid, status_status, health_health, owner) FROM stdin;
\.


--
-- Data for Name: canary_notificationhours; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY canary_notificationhours (parent_id, value, sort_order) FROM stdin;
\.


--
-- Data for Name: canary_watcherids; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY canary_watcherids (parent_id, value, sort_order) FROM stdin;
\.


--
-- Data for Name: canaryanalysis; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY canaryanalysis (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, baseversionid, canaryid, compareversionid, finalscore) FROM stdin;
\.


--
-- Data for Name: canaryanalysis_metricscoreids; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY canaryanalysis_metricscoreids (parent_id, value, sort_order) FROM stdin;
\.


--
-- Data for Name: casservicemetricdetails; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY casservicemetricdetails (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, metricname, metrictype, kairosaggregator) FROM stdin;
5	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.disk.blocks.write	IO KPI	avg
4	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.disk.blocks.read	IO KPI	avg
10	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.cpu.util	Compute KPI	avg
12	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.cpu.numprocesswaiting	Compute KPI	count
11	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.cpu.iowait	Compute KPI	avg
13	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.net.bytes_send	Network Group	avg
15	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.net.packets_in.error	Network Group	count
14	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.net.bytes_rcvd	Network Group	avg
16	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.net.packets_out.drops	Network Group	count
18	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.net.packets_in.drops	Network Group	count
17	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.net.received_rate	Network Group	avg
19	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.net.packets_out.error	Network Group	count
20	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.net.transmit_rate	Network Group	avg
6	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.mem.swap.in	Memory Group	avg
8	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.mem.swap.total	Memory Group	avg
9	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.mem.util	Memory Group	avg
7	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.mem.swap.out	Memory Group	avg
2	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	http.requests	load	sum
1	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	http.responsetime	response	avg
\.


--
-- Data for Name: casservicemetrics; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY casservicemetrics (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, servicename) FROM stdin;
\.


--
-- Data for Name: casservicemetrics_metricsdetails; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY casservicemetrics_metricsdetails (metricsdetails_casservicemetrics, casservicemetricdetails, sort_order) FROM stdin;
\.


--
-- Data for Name: cluster; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cluster (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, name, clustertype, accountname, region, buildid, imageid, iscanarycluster) FROM stdin;
\.


--
-- Data for Name: health; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY health (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, health, message) FROM stdin;
\.


--
-- Data for Name: host; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY host (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, name, ipaddress, domainname, description) FROM stdin;
\.


--
-- Data for Name: metric; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY metric (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, name, metrictype, cummulative, aggregator) FROM stdin;
\.


--
-- Data for Name: metricscore; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY metricscore (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, metricname, score, error, metrictype, version1stats_versionstats, version2stats_versionstats) FROM stdin;
\.


--
-- Data for Name: metricscore_bucketscoreids; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY metricscore_bucketscoreids (parent_id, value, sort_order) FROM stdin;
\.


--
-- Data for Name: ownerwatcherdata; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY ownerwatcherdata (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, name, email) FROM stdin;
\.


--
-- Data for Name: service; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY service (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, name, displayname, description, servicegroup) FROM stdin;
\.


--
-- Data for Name: service_bucketids; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY service_bucketids (parent_id, value, sort_order) FROM stdin;
\.


--
-- Data for Name: service_hostids; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY service_hostids (parent_id, value, sort_order) FROM stdin;
\.


--
-- Data for Name: service_metricids; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY service_metricids (parent_id, value, sort_order) FROM stdin;
\.


--
-- Data for Name: serviceversion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY serviceversion (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, name, serviceid, starttime, endtime) FROM stdin;
\.


--
-- Data for Name: serviceversioncharacterstic; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY serviceversioncharacterstic (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, json) FROM stdin;
\.


--
-- Data for Name: serviceversioncharacterstic_serviceversionid; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY serviceversioncharacterstic_serviceversionid (parent_id, value, sort_order) FROM stdin;
\.


--
-- Data for Name: status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY status (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, status, complete) FROM stdin;
\.


--
-- Data for Name: svcresult; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY svcresult (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, svcid, servicename, versionfirst, versionsecond, result) FROM stdin;
24	n42.domain.model.SVCResult	2016-07-19 19:29:05.436	2016-07-19 19:29:05.436	278017747	t	123	Tomcat	v1.0	v1.1	{"comparison_output":{"serviceName":"Tomcat","version1name":"v1.0","version2name":"v1.1","comparisionScore":30.5555555555556,"results":{"tomcat.currentThreadsBusy":{"metricName":"tomcat.currentThreadsBusy","metricType":"Tomcat","score":0,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":1,"1st Qu.":3,"Median":3,"Mean":3.2721,"3rd Qu.":4,"Max.":7},"version2":{"Min.":0,"1st Qu.":3,"Median":3,"Mean":2.6898,"3rd Qu.":3,"Max.":4}},"quantileinfo":{"version1":{"5%":1,"95%":6},"version2":{"5%":1,"95%":3}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":1,"1st Qu.":3,"Median":3,"Mean":3.2644,"3rd Qu.":4,"Max.":7},"bucket.quantiles":{"5%":1,"95%":5.7},"bucket.raw.data":[3,4,1,1,1,4,3,1,3,1,1,3,6,1,4,1,4,5,4,3,3,3,7,6,3,3,3,5,4,5,3,3,3,5,4,3,3,3,3,3,5,5,3,5,5,4,3,3,3,3,3,3,3,3,3,3,1,1,4,4,4,4,4,5,4,3,6,6,1,3,4,3,1,4,3,4,1,3,4,1,1,5,2,3,1,3,5]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":1,"1st Qu.":3,"Median":3,"Mean":3.2905,"3rd Qu.":4,"Max.":7},"bucket.quantiles":{"5%":1,"95%":6},"bucket.raw.data":[3,4,4,2,2,2,1,1,1,1,1,1,1,1,1,1,1,1,3,4,4,4,4,4,3,3,3,1,1,3,3,4,4,3,1,3,3,3,4,4,3,3,1,3,3,4,3,3,3,3,3,6,6,7,6,7,4,3,3,3,3,3,1,1,1,1,1,1,3,3,3,3,4,3,3,3,3,3,1,1,1,1,1,1,1,3,3,3,3,5,5,6,5,5,4,3,3,3,3,3,3,3,3,3,3,3,3,6,7,7,7,7,6,6,6,6,6,6,6,6,6,6,5,4,3,3,3,3,3,3,3,3,3,3,5,5,5,5,3,4,4,4,4,4,4,4,3,3,3,3,3,3,3,3,3,3,3,5,5,5,5,3,3,3,5,5,5,3,3,3,3,3,3,4,3,3,3,3,3,3,4,3,3,3,4,3,3,3,1,1,1,1,1,1,1,3,3,4,4,4,4,4,4,4,4,4,4,4,4,4,5,4,4,3,3,3,6,6,6,6,6,5,3,5,6,6,6,6,2,1,1,1,1,1,1,3,3,3,4,3,3,3,3,3,3,3,3,3,3,3,1,1,1,1,1,3,3,3,3,3,3,3,3,3,3,4,5,5,4,4,1,1,1,3,3,3,4,5,6,5,5,5,5,3,1,1,1,1,1,1,1,1,1,1,3,3,4,5,5,6,5,4,5,6,6,6,6,6,5,3,4,3,3,3,3,1,1,1,1,1,1,3,3,3,4,3,5]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":1,"1st Qu.":3,"Median":3,"Mean":2.8077,"3rd Qu.":3,"Max.":4},"bucket.quantiles":{"5%":1,"95%":3},"bucket.raw.data":[3,1,3,3,3,4,3,3,3,3,3,3,3,3,1,3,3,3,3,3,3,3,3,3,1,3]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":0,"1st Qu.":3,"Median":3,"Mean":2.6579,"3rd Qu.":3,"Max.":4},"bucket.quantiles":{"5%":1,"95%":3},"bucket.raw.data":[3,3,3,3,3,3,3,1,1,1,1,1,1,1,3,3,3,3,3,3,3,1,3,3,3,3,3,1,3,3,4,4,4,4,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,1,1,1,3,3,3,3,1,1,3,3,3,3,3,1,1,1,1,1,1,1,1,1,3,3,3,3,3,3,1,3,3,3,3,3,1,1,1,3,3,4,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,0,2,4,3,3]}},"bucketscores":["bucket1",0,"bucket2",0]},"tomcat.HeapMemoryUsage":{"metricName":"tomcat.HeapMemoryUsage","metricType":"Tomcat","score":100,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":3563800000,"1st Qu.":4633000000,"Median":5173100000,"Mean":5389900000,"3rd Qu.":5746500000,"Max.":10833000000},"version2":{"Min.":3228700000,"1st Qu.":4681200000,"Median":5180300000,"Mean":5160300000,"3rd Qu.":5656400000,"Max.":7294400000}},"quantileinfo":{"version1":{"5%":3914493982.8,"95%":8001443693.2},"version2":{"5%":3969637888,"95%":6266246280}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":3783000000,"1st Qu.":4759400000,"Median":5342300000,"Mean":5701600000,"3rd Qu.":6453200000,"Max.":10595000000},"bucket.quantiles":{"5%":3985982200,"95%":8456991572.8},"bucket.raw.data":[5045727976,7568690624,6364607768,5046828704,5089814952,4753923768,4918902072,5224705992,5678747992,4662441248,5507789624,3782950592,5539862552,5523244568,3934339688,5594099368,5969541264,4577005064,5450572920,5784082328,4806276432,3984540736,5799731880,4970592992,6960971264,5638887568,6558038648,7075309712,6839188944,6146478896,6886990432,7437081208,8671523080,9663000424,10595065736,6541791952,7293318136,7521913464,8560531360,7667289912,9773118720,7865890752,7954704080,6961040336,6236142592,7436814432,7561383504,8215398736,5342283376,5887565672,5813183640,4182033744,4768421512,4811339440,5335374472,5577841440,5377144176,4764812344,4237816488,4310900400,5173121560,4055932944,4237295936,5012524744,5581683992,3994593288,5239423240,4070486032,5390362312,3989345616,4687228728,5224351720,4518425552,3973688608,5005782992,5089438696,5270273968,4273071208,4946131416,5120449416,5439966856,5668741080,4354184192,4235754352,4345810992,5302546120,3790165832]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":3569900000,"1st Qu.":4521800000,"Median":5051400000,"Mean":5262700000,"3rd Qu.":5646500000,"Max.":9512500000},"bucket.quantiles":{"5%":3883877201.6,"95%":7834602202.4},"bucket.raw.data":[4452172368,5940940416,6684435984,6168660520,6881810096,7644835960,5904222384,6946454520,5708360480,6059281016,6692808272,5603033672,6096727200,4518217736,4827820336,5692142984,5926014208,4234698224,4788878136,4706195784,4992378584,5145527472,5395923760,5733747184,4338303024,4657129152,4890529792,5009332736,5075831520,4457956856,5041126792,5671537656,4040505248,4235215808,4341643208,4953304376,5128056624,5185866008,5246084464,5629074512,4700427008,4806962784,5961606312,4117506008,4786201440,4952768888,5668284208,4191380896,4429009816,4443142416,4731278528,5040714760,5285662728,5461936560,5655060848,5851307360,3881797400,3874503968,3845197080,3862721312,4258110448,4654188576,4845124776,5868430504,3722921664,3952325912,3934515712,4110733160,4309910768,4585816432,4881278736,4795469592,5052640904,4001002024,4337420320,4680401184,4993574464,5271046952,5268704976,5480526496,5457443008,5661263160,4072952920,4254934648,4647460976,5044538792,5133920480,5449160952,4144890888,3952863096,4073076608,4240314752,4294921104,4995974496,6103352200,4525395672,4985839784,5476305080,5850128240,4181373056,4313166808,4566494344,5018933360,5459974744,5769954696,4753476736,4934934728,5212043216,5370110448,5481067152,5508100632,5800496032,4083561848,4425379352,4578479120,4771328552,4718393296,4974087120,4886128696,5033036856,5051367760,5014676016,5087566280,5260266712,5319256784,6162078040,4821706848,6038711544,5322654480,5623217584,6066115912,6803596024,5229127928,6232147184,6064055000,7134294128,6090550848,7994586104,6982137856,7927055888,5800620368,5566270008,6362299736,7313933832,7115315120,7848582080,6993491952,7717128368,8078258744,9120340608,8596903792,7901494936,8587568600,6191589488,8385731808,7691152784,9512461120,8801753568,7402546112,8434350816,6703725000,8776086296,7128529424,8589637536,8011350984,8842966800,7801982488,6526877912,8607809312,7278974040,6405297216,5162378584,4296010960,4616750016,5264657464,4172194048,4250003896,4396046352,4894419480,5467203840,5443526288,6131764312,4993143976,5176361232,5318440128,5424261176,5589573368,5562632464,5901948808,4651447192,4985633208,5219636192,5654170456,5633256160,3805309616,5178639416,5963017216,4148152944,4233624144,4435199968,4474812288,5027654016,4891111296,5065067288,5460469248,6210247864,4349900376,5313041544,5926643112,4268313144,4393714768,5555207880,5861596464,3971915048,4282787704,4531230584,4996930336,4750601440,4858159792,4734040808,4842147048,5500613240,5673611592,5836682408,3569918832,3647356408,4029965792,4472549048,4555629800,4939961256,5156843728,5199579984,5238424384,5179054424,5240901664,5667636648,5542752696,5747016520,5782304808,3717402696,3776792152,3798778536,4114756280,4329870392,4500832400,4814376648,5183088640,5532524544,5525110288,5651347080,3908222696,3914480808,4133989488,5140558352,5263821120,5647598232,3739120192,3945619864,4230408144,4316667776,4438815560,4582295880,4612585504,4773593576,4974872952,4983668680,5035438344,5050132416,5562674232,4135726680,5030268152,4995947912,5107026824,5878822304,4551273416,4885691944,4941704144,4912396752,4960142792,4847060424,5065849496,4907821592,4935687864,5169559056,5157476736,5135336640,5287973360,5272443776,5260133728,5359279976,5369243984,5355917672,5540132952,5618806640,5551771504,5531220960,5554442760,3729519576,3888730072,3975803056,4210703360,4415256592,4410305576,4778971672,4938710552,5736326160,3843806240,3849479072,4188709368,4068131120,4128884832,4450971072,4534309296,4601873784,4903712000,4951122768,5342085152,4905251248,3787924888,4892466440,4878792112,5470118440,5580062552,5536580712,5598857400,5645481008,3691461120]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":3228700000,"1st Qu.":4615000000,"Median":5308000000,"Mean":5149400000,"3rd Qu.":5800800000,"Max.":6431900000},"bucket.quantiles":{"5%":4089214896,"95%":6053937984},"bucket.raw.data":[3228686232,4116463656,5303996368,5312055072,5560096080,5805505472,4237933448,4594221888,4641402352,5349528448,4080131976,4198736584,5501967736,5862879696,5203579392,5858294384,6070008528,5910383400,5327357072,4665794624,5042164928,5786639392,5181681048,4606219600,6431875256,6005726352]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":3593900000,"1st Qu.":4687100000,"Median":5103300000,"Mean":5117400000,"3rd Qu.":5559300000,"Max.":6964200000},"bucket.quantiles":{"5%":3961243386,"95%":6170053058.8},"bucket.raw.data":[3695554616,3593938984,3666610280,3681223208,3643955784,3790468120,4044413120,4552317472,4143008352,3635043232,3967288128,4409284152,4649806416,4712183848,4879478808,4951194424,5239190328,5256177520,5344606056,5661225232,6234970856,4665201904,5086260616,5925029608,6210407640,4443368960,6081947352,6774179368,5336767120,6030170432,4244668000,4800789040,5092459240,5609238928,4693926408,5140113816,5585231952,6126140856,4686003824,5138801064,5510271688,5887582528,6311261000,4760479800,5359984808,5944164432,6168523048,4955961944,5173911368,5480235144,6252195872,6901843504,5482217536,5617688152,6964192352,5818121928,6026585064,4594068448,5154092584,5402549768,5309799672,5542590776,5846278272,5814952056,4243768784,4480951344,5013718784,5065302160,5693035376,6037884592,4468001264,4544175512,4830486600,5086571616,5183442320,5558317328,4843415912,4961516360,5101765960,5180324656,5524089832,5890260912,4426010016,4990923008,5292629632,4724935456,4717875040,4847316096,5015951504,5104753856,5335739056,5194570088,5287893160,5787064600,4526164048,4687412800,4894820752,5029607776,5313494872,5317181496,5374719616,5306515592,5598982832,5867924104,4712560296,5525778688,6155675600,4713699800,5403083272,4310081112,4855445192,5543161808,4335080520,6171923072,5039151336,5329739920,5562327200,5951027944,4410493232,4720692288,4710252040,4944843872,4972562904,5012914136,5406010264,4593857208,4488778024,4811156800,5617910712,6054501376,4288742840,4612136664,4971366024,4981902400,5268395256,6067721352,3953855368,4296879120,4282597512,4548271128,4902725472,5714735480,5731732840,3969637888,4131747112,5155941920,6004074024,4681245656,5013120912,5242925080,5512278832,5110422408]}},"bucketscores":["bucket1",100,"bucket2",100]},"tomcat.Uptime":{"metricName":"tomcat.Uptime","metricType":"Tomcat","score":0,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":1579200,"1st Qu.":2700800,"Median":3829700,"Mean":3829600,"3rd Qu.":4958300,"Max.":6087100},"version2":{"Min.":309770,"1st Qu.":759810,"Median":1210400,"Mean":1209800,"3rd Qu.":1659800,"Max.":2109900}},"quantileinfo":{"version1":{"5%":1797838.65,"95%":5861340.6},"version2":{"5%":399791,"95%":2019802}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":1922100,"1st Qu.":3212200,"Median":4032200,"Mean":3994700,"3rd Qu.":4712200,"Max.":6082100},"bucket.quantiles":{"5%":2120115.2,"95%":5764110.4},"bucket.raw.data":[1922096,1952275,2002129,2052169,2102117,2162111,2242080,2272185,2282083,2352081,2432149,2492097,2562124,2672080,2782114,2882136,2952125,3012816,3032120,3082079,3132094,3172143,3252177,3362085,3432105,3492098,3512195,3562300,3612150,3652122,3702163,3732145,3762123,3772127,3782122,3812407,3822119,3842201,3872161,3892112,3912124,3952106,3982126,4032164,4052244,4062163,4082093,4112090,4142164,4152083,4192126,4202082,4242082,4292086,4322100,4372131,4422097,4462079,4502132,4542094,4572153,4612088,4652120,4682135,4692243,4732085,4812092,4882126,4972187,5052091,5092093,5122087,5192088,5252115,5332113,5392109,5432080,5452093,5472113,5572104,5652081,5722109,5782111,5882083,5982426,6012116,6082082]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":1912100,"1st Qu.":2882100,"Median":3992100,"Mean":3994000,"3rd Qu.":5092100,"Max.":6072100},"bucket.quantiles":{"5%":2125096.8,"95%":5869104.5},"bucket.raw.data":[1912133,1932119,1942347,1962239,1972121,1982169,1992138,2012176,2022123,2032103,2042142,2062170,2072122,2082090,2092098,2112101,2122104,2132080,2142119,2152108,2172081,2182081,2192087,2202088,2212084,2222105,2232082,2252081,2262090,2292085,2302092,2312091,2322100,2332094,2342090,2362084,2372079,2382082,2392117,2402096,2412080,2422080,2442113,2452102,2462084,2472082,2482081,2502080,2512081,2522092,2532111,2542085,2552082,2572079,2582082,2592085,2602089,2612112,2622114,2632084,2642093,2652091,2662108,2682080,2692079,2702081,2712084,2722113,2732109,2742082,2752086,2762088,2772083,2792087,2802112,2812126,2822081,2832118,2842079,2852082,2862197,2872126,2892090,2902100,2912131,2922109,2932095,2942080,2962083,2972112,2982081,2992085,3002089,3022122,3042127,3052138,3062146,3072163,3092114,3102096,3112081,3122134,3142330,3152081,3162119,3182091,3192081,3202080,3212081,3222083,3232145,3242111,3262105,3272083,3282082,3292091,3302093,3312081,3322082,3332089,3342080,3352087,3372101,3382088,3392091,3402142,3412110,3422113,3442119,3452419,3462154,3472198,3482134,3502375,3532109,3542169,3552110,3572129,3582443,3592126,3602119,3622157,3632133,3642107,3662150,3672254,3682101,3692185,3712141,3722169,3742172,3792098,3802137,3832104,3852226,3862254,3882141,3902122,3922256,3932173,3942251,3962134,3972099,3992116,4012111,4022100,4042107,4072102,4092119,4102084,4122139,4132104,4162116,4172099,4182082,4212107,4222081,4232143,4252100,4262078,4272086,4282114,4302102,4312091,4332113,4342081,4352104,4362102,4382145,4392106,4402148,4412117,4432080,4442081,4452147,4472100,4482098,4492088,4512088,4522084,4532108,4552081,4562078,4582117,4592121,4602158,4622087,4632093,4642111,4662079,4672105,4702117,4712109,4722082,4742080,4752101,4762106,4772128,4782084,4792090,4802145,4822086,4832083,4842093,4852090,4862083,4872108,4892081,4902122,4912083,4922085,4932097,4942081,4952081,4962106,4982080,4992090,5002109,5012081,5022081,5032083,5042126,5062086,5072178,5082117,5102111,5112103,5132101,5142100,5152090,5162162,5172103,5182082,5202123,5212081,5222112,5232189,5242080,5262085,5272080,5282087,5292082,5302081,5312078,5322081,5342082,5352152,5362085,5372103,5382112,5402111,5412109,5422081,5442174,5462132,5482082,5492080,5502084,5512098,5522081,5532082,5542091,5552080,5562081,5582092,5592092,5602080,5612110,5622083,5632095,5642089,5662082,5672086,5682110,5692096,5702135,5712084,5732114,5742106,5752086,5762149,5772117,5792088,5802099,5812081,5822092,5832140,5842104,5852126,5862087,5872112,5892106,5902106,5912093,5922080,5932112,5952104,5962082,5972137,5992095,6002140,6022106,6032105,6042094,6052120,6062081,6072082]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":309770,"1st Qu.":774820,"Median":1202300,"Mean":1230000,"3rd Qu.":1682300,"Max.":2084900},"bucket.quantiles":{"5%":434790.5,"95%":2032282.75},"bucket.raw.data":[309771,389787,569801,609812,644790,704796,759813,819841,869782,929836,1059770,1129780,1199826,1204817,1249802,1314813,1519814,1579780,1629780,1699809,1804782,1889783,1954894,2009827,2039768,2084892]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":319770,"1st Qu.":761080,"Median":1204800,"Mean":1200800,"3rd Qu.":1642300,"Max.":2099800},"bucket.quantiles":{"5%":405310.25,"95%":1994326.95},"bucket.raw.data":[319770,329789,339788,349768,359776,369800,379805,399791,409826,419770,429779,439767,449826,459789,469801,479800,489792,499790,509777,524800,534816,549826,559791,579787,589803,594792,619849,629844,639788,654791,664796,679814,689790,699771,719821,729803,739827,749780,764841,779829,789849,799793,809849,829818,839833,849801,859856,879782,889781,899808,909773,919818,939797,949786,959847,969825,979825,989807,999800,1009813,1019790,1029826,1039796,1049815,1069768,1079790,1089811,1099769,1109798,1119793,1139795,1149780,1159807,1169810,1179788,1189773,1219789,1229843,1239783,1259783,1269793,1279812,1289796,1299801,1309785,1329777,1339792,1349770,1359797,1369813,1379803,1389780,1399785,1409808,1419782,1429784,1439806,1449789,1459774,1469778,1479811,1489778,1499768,1509793,1529817,1539781,1549778,1559795,1569833,1589833,1599769,1609825,1619807,1639812,1649805,1659768,1669790,1679802,1689773,1709768,1719783,1729800,1739787,1749774,1759795,1779768,1789770,1799794,1819781,1829792,1839841,1849791,1859813,1869789,1874820,1899853,1909774,1919864,1929793,1939795,1949799,1969803,1979833,1989858,1999789,2019802,2029854,2049794,2059792,2069783,2079815,2099793]}},"bucketscores":["bucket1",0,"bucket2",0]},"tomcat.NonHeapMemoryUsage":{"metricName":"tomcat.NonHeapMemoryUsage","metricType":"Tomcat","score":0,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":415140000,"1st Qu.":428600000,"Median":434790000,"Mean":432570000,"3rd Qu.":438090000,"Max.":440620000},"version2":{"Min.":294750000,"1st Qu.":388210000,"Median":403490000,"Mean":395860000,"3rd Qu.":410320000,"Max.":417720000}},"quantileinfo":{"version1":{"5%":417645789.6,"95%":440214796.8},"version2":{"5%":353236584,"95%":417252784}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":419230000,"1st Qu.":430690000,"Median":435500000,"Mean":434040000,"3rd Qu.":437250000,"Max.":440570000},"bucket.quantiles":{"5%":424856809.6,"95%":440178024},"bucket.raw.data":[419229104,419704096,421511432,425036592,424771304,424779760,425370776,425361592,425950960,425909960,426262824,426651712,427220312,428554264,429002024,429244712,429479776,429811680,429904408,430191640,430164256,430528512,430850000,431653168,433216848,433325192,433424952,434025256,434008816,434293152,434382608,434481056,434620976,434710880,434717264,434784040,434714232,434872248,434841552,434922808,434932304,435000936,435104584,435500568,435629328,435629328,435610624,435738856,435829016,435862904,435961960,436007824,435903296,435964624,436302512,436336272,436280192,436254144,436434248,436552448,436633984,436797256,437120392,437167584,437144552,437329912,437620744,438031976,438179632,438142080,438053112,438982880,439395920,439376752,438903936,439280744,439335824,439419776,439530720,440007320,439880432,440172704,440180304,440303376,440256280,440396592,440574856]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":419350000,"1st Qu.":429370000,"Median":435270000,"Mean":433790000,"3rd Qu.":438230000,"Max.":440620000},"bucket.quantiles":{"5%":425077876,"95%":440238580.8},"bucket.raw.data":[419354264,419552904,419673432,419487520,419632648,419607992,419713728,424998032,425085576,425058312,425127128,425516552,426229296,425632104,424954232,424432704,424527808,424448152,424317768,424592584,424970256,425082272,425075992,425215816,425182080,425295880,425302600,425292032,425318336,425953304,425954936,425924792,425943432,425969848,426015288,426013440,426000592,426027824,426173120,426243240,426347512,426238760,426250080,426930992,426972856,427002248,426883536,426667032,426874728,426995720,427061840,427271344,427246896,427197432,427134536,427090648,427620784,427672312,427699656,427718464,427738136,427754136,428186688,428673904,428552944,428600720,428628904,428546152,428645352,428656696,428669040,428708144,428916240,429018520,429010264,429012864,429297992,429298920,429280752,429350368,429365000,429381256,429368632,429508648,429469320,429513104,429494144,429523800,429676336,429753048,429797024,429805896,429823984,429864984,430013320,430092032,430110624,430100824,430053760,430023744,430088384,430135648,430424472,430414640,430540480,430697976,430752088,430623560,430789536,430809624,430786112,430828464,430969384,430973760,430982608,430840976,430879824,430863896,431256440,431370760,431374048,431468480,431801864,432287024,432385928,432298736,432425000,432531008,433392456,433255672,433333168,433314056,433284328,433412464,433681224,433692992,433810584,434133792,434083592,434068920,433935040,434096392,434158568,434176512,434282048,434301592,434317792,434334616,434456272,434528704,434621464,434572112,434566096,434804120,434870240,434852440,435092832,434979528,434970448,434847112,434973424,435049120,435103304,435266952,435401272,435425720,435517384,435632648,435626136,435695640,435744864,435855168,435982544,436029936,436081136,436094424,436204728,436162664,435952832,435980528,435996112,435955472,436122296,436285104,436443152,436419280,436494224,436526344,436341656,436371104,436268576,436329600,436295504,436298960,436338760,436356320,436293304,436288840,436443232,436476224,436520280,436542816,436619376,436648200,436719984,436718384,436894976,436982688,437027304,437132264,437137592,437177304,437268792,437365736,437348504,437421400,437441480,437471216,437508464,437528432,437631944,437755848,437789456,437871784,437887272,438011560,438025376,438083256,438087816,438100920,438122976,438149112,438150968,438157816,438193656,438149264,438192592,438193544,438207232,438261968,438127504,438131216,438039016,438049680,438033824,438091792,438867416,439036416,439153920,439249448,439268136,439268136,439309808,439529208,439574480,439376296,439393264,439381456,438925920,438762256,438706120,439223120,439025496,439027024,439028488,440053288,440039168,439956160,439890856,439910680,439306984,439356336,439355888,439412104,439447328,439541520,439640744,439640224,439793584,439794240,439799424,439814168,439836960,439997856,440011160,440007288,440004392,439903976,439916648,439960312,439960312,439880432,439887872,439889136,439897520,440125088,440159648,440172720,440173160,440213056,440149896,440179088,440159432,440119944,440200880,440033784,440045752,440083016,440075464,440094536,440290896,440249520,440273040,440258960,440263888,440267952,440322416,440326968,440254720,440345896,440402344,440408016,440416848,440500040,440534224,440615376,440566512]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":294750000,"1st Qu.":388770000,"Median":402960000,"Mean":395430000,"3rd Qu.":410110000,"Max.":417440000},"bucket.quantiles":{"5%":352057384,"95%":417284684},"bucket.raw.data":[294751984,342262800,381441136,384601176,386057856,387467968,388299184,390215512,390163232,393578344,400591176,402336968,402719728,403196200,403593800,404243304,408388488,408398312,409029280,410470008,411682360,412211832,413633240,417218024,417306904,417442520]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":305120000,"1st Qu.":388150000,"Median":403080000,"Mean":395650000,"3rd Qu.":409540000,"Max.":417690000},"bucket.quantiles":{"5%":354966313.6,"95%":417204279.6},"bucket.raw.data":[305117712,310514016,313269408,314395472,315804832,329985136,337427424,353737728,355971520,356868360,361413128,364412104,365496528,372041744,374806584,376521600,377033944,378533416,378700672,379186016,380859824,381076568,380912496,382319608,382726984,383009880,383924464,384806576,384589848,386819440,386753792,386807048,387193912,387751744,387889744,387939744,387655184,387962928,388213840,389339864,390140496,389524504,389702608,390252536,390434456,390417760,390780256,390498544,391020440,390964368,391333240,392179544,394476832,395488896,395233464,395732984,395743064,395919744,397042904,398279096,398331368,399955472,400535016,400590424,400513128,401277040,401448048,401711208,401405088,401670200,402262320,402229976,402359416,402672088,402658624,402623688,403963032,403829496,403489304,403723752,403832768,403761408,403990832,403667984,404158392,404320032,404871456,404904584,404868488,405114648,405137560,405243440,405031952,404998184,405166672,405240176,405526328,406065160,405845008,405711872,405686056,406178408,406242496,408661128,407760616,407560360,407450744,407719168,407660072,408551136,408713600,408710696,408872552,409424336,409889328,410322840,410573360,410811280,410551512,410997200,411482152,411779472,411651904,411361296,411513528,411723728,411671560,411720888,411682336,411923272,411954528,412121424,412115960,412026768,412161176,412410424,412599352,412591416,413881232,415219552,413504000,417032392,417220520,417249448,417221224,417190992,417167928,417430496,417277384,417366232,417258840,417694440]}},"bucketscores":["bucket1",0,"bucket2",0]},"tomcat.rejectedSessions":{"metricName":"tomcat.rejectedSessions","metricType":"Tomcat","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"tomcat.ThreadCount":{"metricName":"tomcat.ThreadCount","metricType":"Tomcat","score":0,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":159,"1st Qu.":415,"Median":680.5,"Mean":634.65,"3rd Qu.":838,"Max.":1080},"version2":{"Min.":172,"1st Qu.":319,"Median":395,"Mean":392.77,"3rd Qu.":478,"Max.":593}},"quantileinfo":{"version1":{"5%":263.15,"95%":1024},"version2":{"5%":224,"95%":571}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":256,"1st Qu.":491.5,"Median":700,"Mean":667.61,"3rd Qu.":807,"Max.":1071},"bucket.quantiles":{"5%":286,"95%":1007.2},"bucket.raw.data":[256,288,286,284,277,271,286,293,297,298,312,336,345,400,403,429,424,475,495,487,479,470,488,526,587,577,596,605,600,633,664,666,664,660,663,669,679,681,699,689,691,695,705,735,722,735,734,704,716,718,700,704,698,707,720,746,737,733,732,736,776,791,796,808,806,784,840,828,830,853,866,912,892,889,920,941,933,939,930,959,970,989,1015,1026,1050,1063,1071]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":248,"1st Qu.":422,"Median":701,"Mean":658.44,"3rd Qu.":876,"Max.":1080},"bucket.quantiles":{"5%":286,"95%":1024},"bucket.raw.data":[248,282,292,290,292,288,290,276,278,280,282,287,289,291,286,267,269,264,263,265,271,273,279,277,279,286,288,288,290,302,301,303,299,301,296,299,300,304,306,305,307,309,323,327,327,328,330,338,333,351,343,347,343,381,383,376,371,377,387,383,392,393,393,400,402,399,397,400,396,389,397,398,401,406,407,409,413,415,417,419,421,426,430,426,422,423,425,426,422,426,432,448,460,492,496,492,487,485,482,485,483,477,483,482,475,471,475,477,487,493,492,492,480,491,503,503,499,513,511,504,506,509,543,563,579,586,586,581,579,569,574,571,576,586,591,587,599,614,612,612,608,598,614,628,641,657,664,670,664,665,659,663,673,679,695,701,699,695,695,694,700,701,707,701,721,721,731,733,729,714,706,708,710,703,702,706,708,704,700,702,715,703,715,714,718,724,726,733,749,751,753,747,742,744,739,735,743,730,734,730,738,748,770,771,782,788,795,799,794,800,807,802,800,796,792,794,795,797,793,803,821,848,850,847,831,831,832,830,831,833,829,831,831,838,839,832,834,836,838,842,844,850,851,866,864,896,902,908,910,917,917,919,899,887,889,887,886,888,891,899,908,904,906,907,917,917,924,930,932,933,943,939,935,937,936,922,926,930,940,952,958,960,967,964,962,964,966,963,970,972,968,976,979,971,978,984,988,999,1003,1005,1011,1013,1019,1024,1014,1022,1023,1024,1024,1022,1024,1036,1046,1053,1055,1051,1050,1053,1049,1059,1061,1070,1072,1080,1070,1072,1074]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":172,"1st Qu.":320,"Median":395.5,"Mean":400.04,"3rd Qu.":478.5,"Max.":581},"bucket.quantiles":{"5%":235.25,"95%":569.25},"bucket.raw.data":[172,222,277,291,275,302,319,323,342,346,357,377,397,398,405,394,453,457,479,477,527,538,557,564,571,581]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":177,"1st Qu.":318,"Median":394.5,"Mean":390.27,"3rd Qu.":475,"Max.":587},"bucket.quantiles":{"5%":226,"95%":560.45},"bucket.raw.data":[177,185,187,192,200,202,216,226,229,230,231,238,241,232,226,227,237,245,247,271,273,279,275,281,283,288,294,290,274,277,282,295,301,307,309,313,309,315,320,323,323,319,322,326,328,330,336,338,342,346,342,345,341,345,336,343,342,338,340,349,347,349,350,352,360,363,363,369,371,375,376,387,383,387,398,396,406,408,410,400,396,403,399,391,393,399,402,404,406,408,410,412,415,417,418,421,422,422,424,426,423,425,435,450,454,450,469,454,462,466,464,463,475,477,479,483,479,475,475,482,488,492,505,499,500,514,522,524,518,529,534,525,521,525,526,540,546,544,548,550,560,561,554,555,557,571,574,578,580,578,580,587]}},"bucketscores":["bucket1",0,"bucket2",0]},"tomcat.activeSessions":{"metricName":"tomcat.activeSessions","metricType":"Tomcat","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"tomcat.sessionCreateRate":{"metricName":"tomcat.sessionCreateRate","metricType":"Tomcat","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"tomcat.bytesSent":{"metricName":"tomcat.bytesSent","metricType":"Tomcat","score":0,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":13287000,"1st Qu.":17709000,"Median":26480000,"Mean":25041000,"3rd Qu.":31983000,"Max.":36787000},"version2":{"Min.":531960,"1st Qu.":2038800,"Median":3500700,"Mean":3300400,"3rd Qu.":4407100,"Max.":6055900}},"quantileinfo":{"version1":{"5%":13372984,"95%":36375705},"version2":{"5%":990715,"95%":5770993}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":15255000,"1st Qu.":19528000,"Median":28099000,"Mean":26221000,"3rd Qu.":30771000,"Max.":36786000},"bucket.quantiles":{"5%":15370180.4,"95%":35872257.9},"bucket.raw.data":[15255176,15261802,15267336,15275637,15281171,15577869,15681094,15686628,15791184,15803267,16108815,16456449,17378420,17706513,17885495,17898425,17953011,18868705,18872364,19062545,19070063,19170047,19885330,21794628,22295935,23030630,23322422,23526942,24291523,25214204,25307937,25410111,25726380,26253462,26356733,26477673,26478133,26480926,26765644,26858916,27202094,27774576,27865122,28098954,28101249,28101249,28203349,28207939,28306257,28308523,28398795,28398795,28410657,28509420,28778808,28786553,28791007,28795461,28932197,29237406,29692603,29950861,30530863,30534033,30536580,31004662,31766138,31975024,31985319,32359096,32618154,32848091,32856135,33043455,33331387,33493929,33497785,33594581,33713331,34771626,34778910,35732757,35932044,36614006,36622476,36633858,36785626]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":15255000,"1st Qu.":17900000,"Median":27868000,"Mean":25857000,"3rd Qu.":32699000,"Max.":36786000},"bucket.quantiles":{"5%":15284768.1,"95%":36542515.7},"bucket.raw.data":[15255176,15259035,15259035,15261802,15264569,15264569,15267336,15270103,15270103,15272870,15272870,15275637,15278404,15278404,15281171,15283938,15283938,15286705,15287161,15577869,15580636,15580636,15675560,15675560,15678327,15678327,15681094,15683861,15683861,15793927,15793927,15797781,15797781,15800524,15800524,15921066,16099547,16099547,16103363,16103363,16106089,16106089,16108815,16334746,16449972,16453757,16453757,16456449,16459141,16560255,16562946,17274551,17277210,17382773,17382773,17386514,17696037,17698656,17698656,17701275,17701275,17703894,17703894,17706513,17709132,17709132,17711751,17711751,17717067,17717067,17720105,17720105,17723816,17888081,17888081,17890667,17890667,17893253,17893253,17895839,17895839,17898425,17901011,17901011,17903597,17913050,17915612,17948397,18157571,18860968,18860968,18864660,18864660,18868705,18872364,18874870,18874870,18877832,19065051,19065051,19067557,19067557,19165027,19167537,19167537,19170047,19267690,19616116,19619704,19878218,19881774,19881774,20092648,20095054,20095054,20199534,20199534,20203063,21308329,21672454,21672454,21701865,21797467,21798383,21885711,21887079,22094417,22197583,22588439,22802651,22803263,22807014,22807014,23121207,23523037,23523647,23526030,23529325,23929487,24131927,24223974,24854051,25036014,25190343,25214816,25304973,25305579,25307937,25405929,25407753,25552000,26383978,26385042,26480466,26573832,26663933,26769354,27087203,27202706,27307288,27502255,27777164,27863016,27867873,28096659,28096659,28098954,28201233,28205644,28205644,28207939,28210234,28308523,28311885,28311885,28401061,28401061,28410657,28412899,28412899,28416215,28416215,28696284,28778200,28782099,28782099,28784326,28784326,28786553,28788780,28788780,28791007,28793234,28793234,28795461,28892649,28892649,28895904,29134907,29134907,29138169,29397012,29690468,29692603,29695813,29695813,30076872,30079007,30079463,30530863,30534033,30718014,31002575,31002575,31004662,31006749,31638775,31761964,31761964,31764051,31764051,31766138,31805078,31969860,31972965,31972965,31975024,31977083,31977083,31979142,31979142,31981201,31981201,31983260,31983260,32175289,32177316,32177316,32180413,32180413,32357069,32357069,32359096,32550593,32551505,32846080,32848091,32850102,32850102,32852113,32852113,32854124,32854124,32856135,32858146,32953002,33024815,33039962,33043455,33112929,33112929,33114884,33327504,33329459,33329459,33487073,33490073,33490073,33492001,33492001,33493929,33495857,33495857,33592681,33594581,33953019,34314521,34580130,34766163,34766163,34767984,34767984,34769805,34769805,34771626,34773447,34773447,34775268,34775268,34777089,34777089,34778910,34780731,34780731,35077366,35077366,35081194,35734525,35734525,35737396,35737396,35739586,35934885,36232952,36234682,36234682,36236412,36374011,36375705,36375705,36614006,36615700,36615700,36617394,36617394,36619088,36620782,36620782,36622476,36624170,36624170,36633858,36635518,36635518,36638303,36639219,36785626]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":531960,"1st Qu.":2084200,"Median":3500700,"Mean":3376300,"3rd Qu.":4707800,"Max.":5872500},"bucket.quantiles":{"5%":1115271.75,"95%":5771036.5},"bucket.raw.data":[531957,990715,1488942,1489058,1489568,1866526,2038804,2220500,2308751,2466454,3112105,3400512,3500659,3500659,3500833,3515627,3872987,4078102,4348185,4827685,4971550,5276699,5572743,5770993,5771051,5872503]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":627100,"1st Qu.":2038800,"Median":3500700,"Mean":3270100,"3rd Qu.":4362800,"Max.":5955400},"bucket.quantiles":{"5%":990746.9,"95%":5770935},"bucket.raw.data":[627095,990541,990541,990599,990599,990657,990657,990715,990773,990773,990831,990831,990889,990889,1078644,1325441,1412464,1412464,1412522,1412522,1412580,1412638,1413094,1488942,1489000,1489000,1489058,1489116,1489568,1615385,1866410,1866468,1866526,1866526,1940115,1940173,1940173,2038804,2038804,2038862,2038920,2141291,2219890,2220558,2220558,2308085,2308693,2388490,2390536,2390536,2390594,2390594,2466906,2610100,2610710,2611220,2611220,2837583,2839425,2839483,3022016,3022016,3112047,3112105,3203405,3302772,3400396,3400396,3400454,3400454,3400512,3400570,3400570,3400628,3500601,3500659,3500717,3500775,3500775,3500833,3515511,3515511,3515569,3515569,3515627,3515685,3515685,3515743,3515743,3515801,3515801,3515859,3515859,3515917,3516357,3516415,3516415,3516473,3516473,3516531,3516531,3872929,3872929,3872987,3873045,3873045,3873103,3873103,3873161,4078160,4179287,4347869,4348127,4348185,4406507,4407117,4505505,4534223,4813889,4828187,4828187,4828697,4876426,4876484,4876484,4971492,4971550,4971550,5071797,5071855,5071855,5168415,5168415,5266151,5276641,5404747,5404805,5506162,5506220,5506220,5506730,5673254,5770877,5770935,5770935,5770993,5771051,5771109,5807749,5807807,5872503,5955395]}},"bucketscores":["bucket1",0,"bucket2",0]},"tomcat.bytesReceived":{"metricName":"tomcat.bytesReceived","metricType":"Tomcat","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"tomcat.errorCount":{"metricName":"tomcat.errorCount","metricType":"Tomcat","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"tomcat.maxTime":{"metricName":"tomcat.maxTime","metricType":"Tomcat","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"tomcat.maxThreads":{"metricName":"tomcat.maxThreads","metricType":"Tomcat","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.disk.blocks.write":{"metricName":"system.disk.blocks.write","metricType":"IO KPI","score":100,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":0,"1st Qu.":28.5,"Median":312,"Mean":1697.7,"3rd Qu.":1410,"Max.":53780},"version2":{"Min.":0,"1st Qu.":18.5,"Median":141,"Mean":1420.8,"3rd Qu.":903,"Max.":99176}},"quantileinfo":{"version1":{"5%":0,"95%":7006.10000000001},"version2":{"5%":0,"95%":5012.09999999999}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":0,"1st Qu.":36,"Median":714,"Mean":2700.1,"3rd Qu.":2950,"Max.":36116},"bucket.quantiles":{"5%":0,"95%":10447.8},"bucket.raw.data":[0,2,50,14,0,0,924,1052,182,27506,13088,1230,16,2362,0,32,7012,734,22702,0,714,784,18,286,1404,6796,2988,2458,1636,250,2912,1724,128,44,24,10806,318,3676,10,4494,174,3450,324,2498,7342,0,1972,6894,512,40,3596,912,548,166,0,8576,0,126,3390,1318,0,702,1126,5558,0,0,3508,1016,4670,1278,52,3260,44,444,814,2998,920,36116,2192,10,9612,0,118,42,26,192,0]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":0,"1st Qu.":24,"Median":290,"Mean":1424,"3rd Qu.":1446,"Max.":35022},"bucket.quantiles":{"5%":0,"95%":5198.2},"bucket.raw.data":[32,4454,32,760,0,3172,22,908,4156,0,50,0,5772,0,0,904,354,15404,778,146,680,0,4832,72,40,1168,3028,1846,1266,3726,42,3422,3110,928,5736,26,0,0,4738,524,2078,1354,570,290,0,18,0,944,2116,1628,1364,2,848,2684,126,390,0,58,0,184,0,1108,34,76,26,42,122,34,1636,334,636,0,1308,418,0,1938,0,42,20,698,30,0,8,674,1104,652,22416,192,806,838,850,2366,1090,0,20,644,214,0,3258,0,662,1486,0,5512,1084,3360,12442,1444,4264,36,2120,426,0,1118,664,3556,0,88,0,46,0,42,270,52,42,1496,1922,480,24,2104,308,498,230,0,228,0,1648,572,376,1970,1598,312,12816,282,250,1542,488,3054,330,4540,166,5446,5212,2432,948,5636,3794,3002,524,290,190,250,484,262,204,184,34,24,3792,40,3228,10940,32,2354,1268,13006,1324,23004,3334,978,1156,522,454,1514,28,0,56,0,0,2418,10,34,60,0,1940,0,2960,2300,5166,1520,1474,35022,0,2866,0,522,1334,666,512,3078,1448,20,3162,5338,76,0,28,134,1864,24,4700,10,254,312,0,130,0,0,0,0,56,0,44,0,4558,288,0,156,0,622,66,0,3920,52,96,64,58,656,108,0,1274,0,690,916,2048,12,1196,68,1610,0,852,0,22,0,2434,22,1854,598,0,34,840,930,1078,21332,632,0,54,16,34,0,0,0,1560,0,30,104,40,2104,24,10,0,0,144,1162,26,36,2656,158,0,1018,0,336,1842,24,0,22,1630,12542,464,1142,0,0,36,0,62,658,204,3776,308,734,1212,0,46,0,392,108,56]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":0,"1st Qu.":43,"Median":142,"Mean":1537.4,"3rd Qu.":524,"Max.":30734},"bucket.quantiles":{"5%":0,"95%":2226},"bucket.raw.data":[298,54,90,1518,584,146,138,114,0,198,30,440,30734,0,46,76,0,2462,378,1294,0,194,4,42,552,580]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":0,"1st Qu.":19,"Median":128,"Mean":1025.4,"3rd Qu.":961,"Max.":14940},"bucket.quantiles":{"5%":0,"95%":3718.59999999999},"bucket.raw.data":[118,0,0,4,0,40,0,0,128,0,48,0,82,148,3036,0,5640,0,56,2124,0,92,3274,68,28,24,30,126,278,4020,1796,0,0,16,30,0,30,0,0,1734,0,32,0,0,2742,380,14,3126,0,104,0,2002,12374,2,14,22,22,0,760,3472,744,242,2972,146,134,2718,0,50,652,2182,14940,426,752,1230,1876,570,24,20,10,338,154,562,48,10,184,52,1632,2764,926,1012,952,906,190,386,56,0,28,0,128,0,414,28,6,22,296,44,732,802,32,20,388,56,2612,706,3408,9066,5444,2576,1938,1518,1346,1396,11778,994,1580,2060,278,4,46,0,3378,24,332,0,892,0,52,2282,0,858,356,558,6022,48,26,224,32,988,384,774,916,80]}},"bucketscores":["bucket1",100,"bucket2",100]},"system.disk.blocks.read":{"metricName":"system.disk.blocks.read","metricType":"IO KPI","score":50,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":0,"1st Qu.":0,"Median":4,"Mean":327.91,"3rd Qu.":137.5,"Max.":23272},"version2":{"Min.":0,"1st Qu.":0,"Median":2,"Mean":244.96,"3rd Qu.":122,"Max.":26882}},"quantileinfo":{"version1":{"5%":0,"95%":1990.4},"version2":{"5%":0,"95%":701.899999999999}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":34,"Mean":576.07,"3rd Qu.":437,"Max.":13464},"bucket.quantiles":{"5%":0,"95%":2384.6},"bucket.raw.data":[0,0,636,0,0,892,4,186,0,4,18,4,0,0,2504,0,16,102,576,0,16,14,0,2,520,78,34,40,26,4,18,6,550,798,0,1358,146,2,1450,64,4,24,4,324,8054,0,502,258,13464,46,426,12,152,120,0,3290,0,238,902,4,882,322,564,3688,276,0,562,0,132,2106,0,0,0,6,70,214,160,964,38,0,90,0,1704,0,0,448,0]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":4,"Mean":256.66,"3rd Qu.":69,"Max.":10464},"bucket.quantiles":{"5%":0,"95%":1408},"bucket.raw.data":[0,0,0,0,0,0,0,0,0,0,0,62,0,0,0,12,6,4,8,0,174,0,0,2,0,4,2,4,4,0,0,22,2,102,8,0,0,0,0,0,0,32,8,0,0,0,0,766,158,4,44,0,0,6,162,128,154,0,0,8,0,0,38,2,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,4,30,8,252,2,4,4,484,12,1850,0,0,24,280,16,0,0,0,694,1250,2,314,4,228,4,4,54,8,0,1528,52,20,0,0,386,938,0,0,0,14,0,0,0,592,6,104,10464,24,42,42,0,2,362,6,6,198,92,16,696,106,6,4,8,4,4,4,70,50,210,606,114,202,2,206,2674,10,68,224,4,1384,768,172,0,0,1028,1266,0,654,642,0,314,788,114,4,3830,32,480,2,4,90,54,0,0,0,0,614,120,18,0,0,0,2,0,146,4,564,4,4,62,0,0,64,28,2,242,8,106,2,0,0,0,0,0,3074,0,0,0,686,410,3590,136,0,0,0,0,0,0,0,0,0,0,16,300,0,1710,2,6,0,0,5082,0,0,0,0,60,0,0,0,3552,266,152,4,0,166,290,0,0,766,0,0,0,4,1394,3414,6,10,0,4,166,4,1414,14,508,6,0,232,2,0,0,2,0,0,0,0,0,2664,0,0,0,3168,12,2896,0,68,0,54,200,0,0,6,2432,42,0,114,234,4,0,0,0,0,0,0,144,3182,0,4,4,4,0,0,0,2,4,0]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":511.77,"3rd Qu.":104.5,"Max.":9714},"bucket.quantiles":{"5%":0,"95%":1606.5},"bucket.raw.data":[9714,684,0,0,1914,2,0,0,0,88,0,228,318,0,0,0,0,0,156,110,0,2,0,0,4,86]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":2,"Mean":232.17,"3rd Qu.":156.5,"Max.":14364},"bucket.quantiles":{"5%":0,"95%":711.299999999996},"bucket.raw.data":[292,56,0,0,0,0,998,0,0,14,198,0,0,6,56,0,0,0,0,0,0,0,254,344,0,0,0,2,40,884,102,12,0,0,0,0,0,0,0,0,0,0,0,0,2,1262,0,0,0,0,0,254,32,0,2240,72,28,0,102,372,170,290,0,2002,34,2,0,0,2,242,52,354,190,304,194,166,0,0,0,0,182,0,0,0,14364,0,460,186,548,270,384,276,180,16,0,0,2,0,0,0,186,0,0,0,164,0,0,0,92,0,116,0,188,100,368,504,570,256,96,12,4,14,2,4,18,178,42,1288,154,0,0,152,4,0,0,0,0,0,0,26,10,16,0,0,0,0,0,1906,154,46,66,62]}},"bucketscores":["bucket1",0,"bucket2",100]},"system.cpu.util":{"metricName":"system.cpu.util","metricType":"Compute KPI","score":0,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":5,"1st Qu.":36,"Median":63,"Mean":61.972,"3rd Qu.":93,"Max.":97},"version2":{"Min.":5,"1st Qu.":82.25,"Median":93,"Mean":85.418,"3rd Qu.":95,"Max.":98}},"quantileinfo":{"version1":{"5%":17,"95%":96},"version2":{"5%":48,"95%":97}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":16,"1st Qu.":40,"Median":82,"Mean":68.77,"3rd Qu.":94,"Max.":97},"bucket.quantiles":{"5%":19.9,"95%":96},"bucket.raw.data":[95,96,95,92,94,94,31,95,79,35,95,27,22,40,81,17,76,19,94,89,94,71,91,25,92,34,94,93,94,97,95,95,93,95,96,87,94,96,95,95,96,97,96,92,85,96,95,95,83,91,49,52,85,92,60,52,93,82,49,51,95,82,78,80,34,40,47,29,29,67,40,77,65,26,43,60,33,48,37,23,16,38,29,17,65,84,18]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":9,"1st Qu.":31.5,"Median":54,"Mean":57.263,"3rd Qu.":90.5,"Max.":97},"bucket.quantiles":{"5%":16,"95%":96},"bucket.raw.data":[90,94,92,95,92,95,93,97,94,92,97,95,93,91,94,95,97,91,39,51,90,48,80,74,75,68,63,28,34,78,56,65,74,57,65,38,25,22,61,78,75,61,95,63,32,27,31,52,62,47,47,38,37,35,37,28,20,23,25,17,36,34,48,67,57,43,15,31,52,84,59,40,58,36,32,57,54,75,62,54,39,19,27,96,96,97,52,44,38,40,27,43,78,93,88,93,86,81,97,70,67,54,95,50,84,51,47,36,62,52,51,58,36,50,37,37,24,74,36,13,12,28,34,55,59,94,94,95,93,71,89,96,91,94,86,92,95,96,97,93,96,97,92,95,96,96,88,95,96,96,92,92,94,96,95,93,94,91,91,94,93,96,94,93,96,95,93,93,96,92,96,96,93,92,79,58,62,67,39,37,88,93,47,76,58,32,19,72,95,95,95,94,84,84,92,68,83,80,25,35,56,92,49,84,95,85,55,94,86,41,50,30,36,37,47,19,44,17,16,22,34,34,34,65,34,29,30,44,38,17,15,22,11,30,18,24,20,18,11,14,15,20,82,55,53,47,74,68,50,60,84,78,74,68,50,42,39,27,24,30,27,63,19,30,64,21,91,77,50,92,37,38,21,59,25,23,23,14,16,14,25,19,24,34,23,12,18,20,39,23,18,9,14,21,12,11,14,29,37,31,28,34,27,22,40,54,31,29,73,28,24,38,34,72,30,51,94,54,30,81,77,53,62,51,28,33,35]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":40,"1st Qu.":90.25,"Median":92.5,"Mean":89.577,"3rd Qu.":95.75,"Max.":96},"bucket.quantiles":{"5%":68,"95%":96},"bucket.raw.data":[40,96,96,96,96,89,95,96,93,96,90,91,90,96,91,88,91,91,95,62,92,93,91,94,95,86]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":5,"1st Qu.":79.75,"Median":93,"Mean":85.25,"3rd Qu.":95,"Max.":98},"bucket.quantiles":{"5%":53.75,"95%":97},"bucket.raw.data":[91,57,16,5,10,70,97,97,96,97,89,71,91,92,98,94,89,72,71,95,96,95,95,95,95,95,96,93,94,94,95,96,95,95,93,94,96,96,96,95,95,95,97,95,94,93,97,70,95,96,95,87,94,95,96,94,90,95,95,88,93,95,95,56,83,80,73,88,92,76,95,94,96,93,92,95,95,95,96,76,89,82,88,94,89,96,92,90,71,64,96,94,96,96,97,94,94,72,48,50,74,77,58,97,95,94,91,96,95,95,91,77,94,92,88,79,64,88,65,36,70,84,51,87,88,80,69,67,67,89,94,65,81,75,76,87,79,88,41,97,93,90,94,77,92,93,95,96,94,94,83,73]}},"bucketscores":["bucket1",0,"bucket2",0]},"system.cpu.numprocesswaiting":{"metricName":"system.cpu.numprocesswaiting","metricType":"Compute KPI","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.cpu.iowait":{"metricName":"system.cpu.iowait","metricType":"Compute KPI","score":0,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.48949,"3rd Qu.":0,"Max.":20},"version2":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.21333,"3rd Qu.":0,"Max.":33}},"quantileinfo":{"version1":{"5%":0,"95%":2},"version2":{"5%":0,"95%":1}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.90805,"3rd Qu.":0,"Max.":20},"bucket.quantiles":{"5%":0,"95%":5.7},"bucket.raw.data":[0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,20,0,0,0,0,0,0,0,10,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,5,0,9,0,0,0,0,2,0,0,1,0,0,0,1,4,7,0,1,0,2,0,0,0,0,0,0,0,0,4,6,0,1,0,1,0,0,0,0]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.33639,"3rd Qu.":0,"Max.":11},"bucket.quantiles":{"5%":0,"95%":2},"bucket.raw.data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,1,0,0,3,0,0,1,11,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,3,0,2,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,1,1,0,0,3,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,7,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,4,0,0,0,2,2,5,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,2,0,0,0,0,0,0,0,0,2,1,0,1,0,0,1,0,0,1,0,0,0,0,1,1,0,0,0,0,6,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,2,0,5,0,1,0,0,1,0,0,0,3,0,0,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.11538,"3rd Qu.":0,"Max.":3},"bucket.quantiles":{"5%":0,"95%":0},"bucket.raw.data":[3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.125,"3rd Qu.":0,"Max.":5},"bucket.quantiles":{"5%":0,"95%":0.449999999999989},"bucket.raw.data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,5,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5]}},"bucketscores":["bucket1",0,"bucket2",0]},"system.net.bytes_send":{"metricName":"system.net.bytes_send","metricType":"Network Group","score":0,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":0,"1st Qu.":46410,"Median":66102,"Mean":69600,"3rd Qu.":88451,"Max.":187500},"version2":{"Min.":10904,"1st Qu.":40018,"Median":54086,"Mean":54279,"3rd Qu.":66758,"Max.":111690}},"quantileinfo":{"version1":{"5%":28222.2,"95%":126634.4},"version2":{"5%":23676.325,"95%":88593.475}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":0,"1st Qu.":45784,"Median":66102,"Mean":67070,"3rd Qu.":83173,"Max.":144640},"bucket.quantiles":{"5%":24511.7,"95%":114601.8},"bucket.raw.data":[109842,101698,82849.5,86052.5,41871.5,43033.5,39085.5,70688.5,24201.5,53843.5,36333,45356,72719,82932,81284,49336,49045.5,74509,126639,76018,65334,89079.5,39200.5,66102.5,106816.5,144640,113318,69825.5,111275,104422.5,80964,92159.5,82834,65017,60926,66769,100106,119084,87632,68350.5,84605.5,71671.5,73976.5,118130.5,83414,67085,70449,62924.5,62788.5,36540,63442.5,50224,61995,46212.5,86867,54982,22124.5,31611,0,44567.5,48547.5,106008.5,105927,62245,47599.5,45041.5,73550.5,60721,30421.5,50925.5,78858,115152,61064.5,59404.5,44277.5,66631,20878.5,22771.5,26965,25235.5,29081,67904,37960.5,42517.5,80615,55624,94327]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":15528,"1st Qu.":44269,"Median":64337,"Mean":67823,"3rd Qu.":85463,"Max.":187500},"bucket.quantiles":{"5%":29209.9,"95%":119125.05},"bucket.raw.data":[118483.5,121109,111390.5,87029,68777,66394,77156,108844.5,187505,81385,84653,112265,104808.5,124683.5,82137.5,37017.5,46709.5,39980.5,38100,55625.5,67246,70661,107996.5,57491,89689.5,75087.5,49474,30882.5,33075,38187.5,79364.5,63142.5,30249,50199,46368,21014,66891,59325.5,84753.5,47076,36215.5,33469,30694,48179.5,34126.5,76479.5,33895.5,31030.5,37998.5,48316,91740.5,135931.5,69545.5,111564.5,92904,73989,73173.5,70014,85339.5,112163.5,145058.5,94124.5,80994.5,89355,43565.5,41815.5,27686.5,52896,50085.5,48585.5,86828.5,45155,119400,68867,57337,76536,82036.5,113161.5,55986,89557.5,73096.5,47268,54552.5,39535.5,74170,29562,77470.5,34059,74769.5,72048,48003,81129,73137.5,155558.5,92809,89316.5,52807,96436.5,84243,62753,68646,58644,59066,54500.5,43293.5,38473.5,64219.5,58237,58377,79510.5,88870,33685,84501.5,47209.5,56909,103660.5,57197.5,56154,91488.5,38228,44115.5,75242,116533.5,110514,127794.5,166866,116324.5,103238,80771.5,135447.5,108250.5,81259.5,106741.5,132875.5,145780.5,59510.5,116388.5,90222,72533,84393.5,64890.5,83746.5,106250,109845.5,80064,112118,93762.5,105724,87238.5,109419,74944,74366.5,85710.5,145173,131467.5,137730.5,82223.5,72928.5,63115,73655,71939,81324,88223,100936,110731,103129,97309,77500,78977.5,67133.5,71030,63825,54179.5,69517.5,74127,63691.5,51946.5,73402,88093.5,37060,62298,40713,85587,89672.5,73061.5,59459,48679,83814,28576,26691.5,49064.5,29808,36196,41873,36742,29059,31911.5,34403.5,37803.5,19767.5,53090.5,38265.5,34236,64337,52072,53557.5,158057.5,83564.5,95568,94010.5,79195,84005,75122.5,51316.5,53492,32300,73729,89364,55612.5,63195.5,92388,52565,51152,109585,54632,88100,105384,56159.5,43484.5,35204,52953.5,40591.5,34724.5,25604.5,38352,31542.5,42133.5,35869,23732,22523,37522.5,36383,63476.5,71847.5,91991.5,88802.5,84620.5,91855,124798.5,75933,79731.5,62186.5,51467,56647.5,24736,67289,64969,42799.5,61963,59837.5,71281.5,70598.5,59060.5,44284.5,89914,69528.5,44551,68441.5,89346.5,58717.5,67436,42796,22328.5,34380,35871,20048,35689,35189.5,50174.5,75956,69588.5,68579,36408.5,56072,43392.5,21324,28645,46445,45924,46409.5,45218.5,15528,17522,17629,45065.5,34280,29643.5,52775.5,38595.5,47152,89450.5,44254,45607.5,40203.5,62412,30885,47648,33338.5,53683,51905,35018.5,66677,43746.5,84344.5,51728.5,40725.5,35000.5,62017.5,107099,54059,79184,95410,111251,91133,78867.5,112843.5,68094.5]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":17620,"1st Qu.":35838,"Median":51171,"Mean":51743,"3rd Qu.":64112,"Max.":103200},"bucket.quantiles":{"5%":23542.35,"95%":77727.475},"bucket.raw.data":[21849,103195.5,73164.5,67928,76828.5,50867,58882,64030,68632,56134.5,30081,28768,35607,50527.5,51475,31079,44681,17620.5,48821,25612,30919,36530.5,78463,41686.5,63950.5,72507.5,64139,62434.5,43727,52155.5]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":10904,"1st Qu.":41157,"Median":52707,"Mean":54049,"3rd Qu.":66921,"Max.":107790},"bucket.quantiles":{"5%":22620.025,"95%":87668.075},"bucket.raw.data":[52341.5,41348.5,23668,22357.5,22744.5,65828.5,94186,85191,87097.5,45320,67935,76815.5,78559.5,56556.5,63135.5,93012.5,64680,87712,61175.5,41089,63390,64262.5,47342.5,59123,80808.5,66160,57547.5,52334.5,41873.5,81926,107787.5,79177,78452,46894.5,46815.5,45710,54551,58478.5,45350.5,58240.5,67134,72325,63852,78923.5,76298.5,48293,76912,87586.5,97797,74999.5,46208,49618,31682.5,31535.5,32114,59494.5,44115.5,63058,44523.5,26896.5,37179,51091.5,66459.5,82471,48451.5,68221,52535,42115,54241,52612,43528.5,58270,75221.5,68452.5,38807,38846.5,29279,29335,31307.5,36109,48457.5,53930,99988,18713.5,30846,43500.5,41462,37854,26546,19025.5,12141,13001.5,10903.5,22553,20904.5,25219.5,25835,36606.5,59202.5,38696.5,46185,59931,30843.5,44312,31555.5,41179.5,50580.5,49109,38521.5,33764,56863.5,47826,44733.5,26491,37971.5,40006,48780,44661,52802.5,70959,48856,66850,69527,72542,61441.5,61792,58267,65218.5,95835,70958,54719,66004,57041,89122.5,71755.5,51487.5,43269.5,61523.5,50524,55145,68979,65898.5,67707.5,72388.5,54534.5,55536,78553.5,36373]}},"bucketscores":["bucket1",0,"bucket2",0]},"system.net.packets_in.error":{"metricName":"system.net.packets_in.error","metricType":"Network Group","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.bytes_rcvd":{"metricName":"system.net.bytes_rcvd","metricType":"Network Group","score":50,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":63897,"1st Qu.":557820,"Median":933530,"Mean":1122100,"3rd Qu.":1482400,"Max.":5458100},"version2":{"Min.":70019,"1st Qu.":706120,"Median":1097000,"Mean":1228900,"3rd Qu.":1601100,"Max.":4755300}},"quantileinfo":{"version1":{"5%":259074.5,"95%":2699099.8},"version2":{"5%":241686.95,"95%":2576132.1}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":63897,"1st Qu.":526540,"Median":945570,"Mean":1066500,"3rd Qu.":1368300,"Max.":3652800},"bucket.quantiles":{"5%":299335.1,"95%":2177439.65},"bucket.raw.data":[1382516.5,1614355.5,1024872.5,2156198.5,734006.5,463983,644031,2962812,772734.5,959205,485531,383476,885326,1149127.5,1740462.5,1250528.5,538374,1206158,3652801.5,783537,756475.5,1826712,828707.5,510343.5,1712360,1400269.5,800994.5,1099080.5,2723578,2186543,1346031.5,950133,945574.5,1042549.5,854978.5,714228,1405488,2946478.5,1001816.5,1736868,1214153.5,1138423,710914.5,2040043.5,1415448,944727.5,1624162,1282810.5,1354010,441086.5,1559118,551773.5,1118100.5,656171,1888400.5,507386.5,362774.5,506086.5,359872.5,305479,426733.5,1148313.5,1219823,730799,296702,441811,671589,496834,237616,228679.5,1598359,2047647.5,1045135,771559,448050,672947.5,63897,239121,465114.5,431538.5,514715,1913239.5,653356.5,1177221,1125892.5,1220983,939542.5]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":108980,"1st Qu.":533940,"Median":887790,"Mean":1093700,"3rd Qu.":1453200,"Max.":4555700},"bucket.quantiles":{"5%":252764,"95%":2632248.1},"bucket.raw.data":[1251586.5,1497054,2330933.5,1484802,1166136.5,1198283.5,994341.5,697096.5,1866430,889500,1765211.5,1707753.5,2190515.5,1409735,1206406.5,517002.5,799511.5,798097.5,488245,730849,424983,1208430.5,1453180.5,667529.5,2070011.5,1142190,609998.5,685446,805152.5,1475284.5,726174.5,1827067.5,533481,1204930.5,985811,287925,1034177,884038.5,1253595,688924,295577,408732.5,286236.5,646602,233737,798489.5,411873.5,151493,429222,675584,1532612,3429445.5,926462,1023207.5,2246517,564346.5,609063,657897,1126068.5,2588716.5,4555655,1119003,790791,1868299.5,436653.5,684957.5,206731,1161614,1040824,1055564.5,1768696,570645,2040251.5,1019470.5,879305,1882480,2006911,2553331.5,481359,1704496.5,1057362,568708.5,534395.5,690439,1296523,887787.5,1629596.5,363920.5,1933398,948706.5,681708,699773,818194,3301408.5,2193593,1650688,878403.5,2139460.5,628422,674387.5,521382.5,839208,951535.5,739961.5,822905.5,755912.5,925973,620864,700423.5,949030.5,933791.5,431937,451085,512914,446776,1685135.5,653885.5,391753.5,858328,230617,232685,1361242.5,768508.5,1160868,1582174,3800753.5,1483368.5,2328763,1360464.5,2650904.5,2184027,628792,2180210.5,1778748.5,1126545.5,1135237.5,1241144.5,1224274,1012778,1780569.5,1492101,1031918.5,791929.5,2517494,2085667.5,1070688.5,1715742,2169394.5,1076812,2002862.5,829584,1283639.5,1650630.5,2703509,2691560,3977323,1034304,1173915,1059880,710359.5,1132879.5,1823247.5,788103,1176654,2403283,3121187.5,1734218.5,1336448.5,2437039.5,1872279.5,1085322,1005984,1367501,1453147,1219728.5,644709,494973.5,1620033.5,968276,466194.5,2030912.5,268157.5,2058011,1897191,1002748,803445.5,394533.5,1944228.5,371625,273856,1506134,724179,1031107,280303,265273.5,497530,396479.5,672741,584029.5,168644,373746.5,440181.5,336744,366920,215988.5,913191.5,3217899.5,888882,699013.5,1671389.5,932702,910046.5,1215974,787668,812741.5,255935,653033.5,1760312.5,261136,373114.5,640137,403123.5,673809,2773084.5,874202,2052881.5,2948952,806882.5,463488.5,365526.5,495429.5,298300.5,418666.5,298018,524801,383708.5,855016,372636,282809.5,203918,261263.5,307972,484940,850448.5,1494550.5,902129.5,966645,1241926.5,2872768,1074118.5,1936212.5,514633.5,606992,743212,237055.5,1486210,593300.5,393904.5,1581887.5,619276.5,1046665,540255.5,551552.5,363480.5,1066137,784048.5,465794.5,977464,647140.5,885787,795777,403476,457651,657910,324008.5,132520.5,407308.5,169235.5,210504,1450944,615532.5,1078559,432531.5,1178941.5,825588.5,182137,251405,767283,557736.5,788871.5,1279463,151912,147200,108979,1102880,616793.5,381794,1349455,991984,977123,2983310,570473,787875.5,670608,1384138,458686.5,1251698.5,359640.5,1073425,1234393,423075,1664958.5,628673.5,2384359.5,816785.5,409137,382348.5,1720725.5,3969705.5,1119058,2491920,2514819.5,3378727,2949185.5,2122223,1217805,914297]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":106070,"1st Qu.":599650,"Median":1129600,"Mean":1179500,"3rd Qu.":1531400,"Max.":3491800},"bucket.quantiles":{"5%":334211.3,"95%":2572283.075},"bucket.raw.data":[106072.5,1554140.5,1463373.5,1229097.5,2776289,1185279.5,1589906,2322942.5,2134311,978160.5,593678,436556.5,740921,654312,1046237.5,551415.5,1073857,296666,1322985,617558,380100,525726,3491822.5,501397,1331267.5,1596249,1186979.5,1233444.5,844415.5,1618869]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":70019,"1st Qu.":705610,"Median":1090000,"Mean":1207600,"3rd Qu.":1580300,"Max.":4755300},"bucket.quantiles":{"5%":243941.2,"95%":2347434.3},"bucket.raw.data":[725592.5,195003,84584,70019,80848.5,377231.5,1189020.5,1411054,1261187.5,1003993,1831035.5,2121302,2229210.5,954618.5,1439539.5,2126858.5,1069256.5,2103602,2027323,1601709,1481039,1615881,704579,1227915,2872591.5,2036991,1871742.5,1220667.5,703305,3104551,3101082.5,2348478,2503883.5,815582.5,1056846,903762,1445525.5,994295.5,999036,1448275.5,1595724,2359829,1384658,2293601.5,2217624.5,1054143.5,1338552.5,2092087.5,2246391,1818267,1735690.5,1056548.5,598864,705956,914511.5,1916188,724198,1493750,1403716.5,716184,812841.5,1053437.5,1962451,2345496,854326,2051681,1256095.5,848618,970691,993060.5,838610.5,1376135.5,1633460,1961200.5,1082657.5,726527.5,592496.5,490268,478473,576263,1522602,2126695.5,4755271.5,260238,727425.5,1150118,1263894.5,662049.5,376356,275972.5,116382.5,147115,170910.5,404228,456238.5,572293.5,286284,482118,1358801,329678.5,1288511,1506853,511994,996446,451042,540102.5,632778.5,801284,570549.5,731603,1421019.5,1286436,1057343.5,235166,515773,566698.5,590958.5,581868.5,770410.5,1668190,962244.5,1444478.5,1101883,1398065,1303897,950912,1522132,778440,3187012.5,1646672.5,770338.5,1575198,1021888.5,1853502.5,1641352,1167073.5,424673,1122473.5,1225799,1114303,1097272.5,1123231,1325630,1516826.5,856828.5,1063937,1788111.5,634288.5]}},"bucketscores":["bucket1",100,"bucket2",0]},"system.net.packets_out.drops":{"metricName":"system.net.packets_out.drops","metricType":"Network Group","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.packets_in.drops":{"metricName":"system.net.packets_in.drops","metricType":"Network Group","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.received_rate":{"metricName":"system.net.received_rate","metricType":"Network Group","score":50,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":62.399,"1st Qu.":544.74,"Median":911.65,"Mean":1095.8,"3rd Qu.":1447.7,"Max.":5330.2},"version2":{"Min.":68.378,"1st Qu.":689.57,"Median":1071.3,"Mean":1200.1,"3rd Qu.":1563.5,"Max.":4643.8}},"quantileinfo":{"version1":{"5%":253.00244140625,"95%":2635.8396484375},"version2":{"5%":236.022412109375,"95%":2515.75400390625}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":62.399,"1st Qu.":514.2,"Median":923.41,"Mean":1041.5,"3rd Qu.":1336.2,"Max.":3567.2},"bucket.quantiles":{"5%":292.31943359375,"95%":2126.40590820312},"bucket.raw.data":[1350.11376953125,1576.51904296875,1000.85205078125,2105.66259765625,716.80322265625,453.1083984375,628.9365234375,2893.37109375,754.62353515625,936.7236328125,474.1513671875,374.48828125,864.576171875,1122.19482421875,1699.67041015625,1221.21923828125,525.755859375,1177.888671875,3567.18896484375,765.1728515625,738.74560546875,1783.8984375,809.28466796875,498.38232421875,1672.2265625,1367.45068359375,782.22119140625,1073.32080078125,2659.744140625,2135.2958984375,1314.48388671875,927.8642578125,923.41259765625,1018.11474609375,834.93994140625,697.48828125,1372.546875,2877.42041015625,978.33642578125,1696.16015625,1185.69677734375,1111.7412109375,694.25244140625,1992.22998046875,1382.2734375,922.58544921875,1586.095703125,1252.74462890625,1322.275390625,430.74853515625,1522.576171875,538.84130859375,1091.89501953125,640.7919921875,1844.14111328125,495.49462890625,354.27197265625,494.22509765625,351.43798828125,298.3193359375,416.73193359375,1121.39990234375,1191.2333984375,713.6708984375,289.748046875,431.4560546875,655.8486328125,485.189453125,232.046875,223.31982421875,1560.8974609375,1999.65576171875,1020.6396484375,753.4755859375,437.548828125,657.17529296875,62.3994140625,233.5166015625,454.21337890625,421.42431640625,502.6513671875,1868.39794921875,638.04345703125,1149.6298828125,1099.50439453125,1192.3662109375,917.52197265625]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":106.42,"1st Qu.":521.42,"Median":866.98,"Mean":1068.1,"3rd Qu.":1419.1,"Max.":4448.9},"bucket.quantiles":{"5%":246.83984375,"95%":2570.55478515625},"bucket.raw.data":[1222.25244140625,1461.966796875,2276.30224609375,1450.001953125,1138.80517578125,1170.19873046875,971.03662109375,680.75830078125,1822.685546875,868.65234375,1723.83935546875,1667.72802734375,2139.17529296875,1376.6943359375,1178.13134765625,504.88525390625,780.77294921875,779.39208984375,476.8017578125,713.7197265625,415.0224609375,1180.10791015625,1419.12158203125,651.88427734375,2021.49560546875,1115.419921875,595.70166015625,669.380859375,786.28173828125,1440.70751953125,709.15478515625,1784.24560546875,520.9775390625,1176.68994140625,962.7060546875,281.1767578125,1009.9384765625,863.31884765625,1224.2138671875,672.77734375,288.6494140625,399.15283203125,279.52783203125,631.447265625,228.2587890625,779.77490234375,402.22021484375,147.9423828125,419.162109375,659.75,1496.69140625,3349.06787109375,904.748046875,999.22607421875,2193.8642578125,551.11962890625,594.7880859375,642.4775390625,1099.67626953125,2528.04345703125,4448.8818359375,1092.7763671875,772.2568359375,1824.51123046875,426.41943359375,668.90380859375,201.8857421875,1134.388671875,1016.4296875,1030.82470703125,1727.2421875,557.2705078125,1992.43310546875,995.57666015625,858.6962890625,1838.359375,1959.8740234375,2493.48779296875,470.0771484375,1664.54736328125,1032.580078125,555.37939453125,521.87060546875,674.2568359375,1266.1357421875,866.97998046875,1591.40283203125,355.39111328125,1888.083984375,926.47119140625,665.73046875,683.3720703125,799.017578125,3224.03173828125,2142.1806640625,1612,857.81591796875,2089.31689453125,613.693359375,658.58154296875,509.16259765625,819.5390625,929.23388671875,722.61865234375,803.61865234375,738.19580078125,904.2705078125,606.3125,684.00732421875,926.78759765625,911.90576171875,421.8134765625,440.5126953125,500.892578125,436.3046875,1645.64013671875,638.56005859375,382.57177734375,838.2109375,225.2119140625,227.2314453125,1329.33837890625,750.49658203125,1133.66015625,1545.091796875,3711.67333984375,1448.60205078125,2274.1826171875,1328.57861328125,2588.77392578125,2132.8388671875,614.0546875,2129.11181640625,1737.05908203125,1100.14208984375,1108.63037109375,1212.05517578125,1195.580078125,989.041015625,1738.83740234375,1457.1298828125,1007.73291015625,773.36865234375,2458.490234375,2036.78466796875,1045.59423828125,1675.529296875,2118.54931640625,1051.57421875,1955.92041015625,810.140625,1253.55419921875,1611.94384765625,2640.1455078125,2628.4765625,3884.1044921875,1010.0625,1146.4013671875,1035.0390625,693.71044921875,1106.32763671875,1780.51513671875,769.6318359375,1149.076171875,2346.9560546875,3048.03466796875,1693.57275390625,1305.12548828125,2379.92138671875,1828.39794921875,1059.884765625,982.40625,1335.4501953125,1419.0888671875,1191.14111328125,629.5986328125,483.37255859375,1582.06396484375,945.58203125,455.26806640625,1983.31298828125,261.87255859375,2009.7763671875,1852.7255859375,979.24609375,784.61474609375,385.28662109375,1898.66064453125,362.9150390625,267.4375,1470.833984375,707.2060546875,1006.9404296875,273.7333984375,259.05615234375,485.869140625,387.18701171875,656.9736328125,570.34130859375,164.69140625,364.98681640625,429.86474609375,328.8515625,358.3203125,210.92626953125,891.78857421875,3142.47998046875,868.048828125,682.63037109375,1632.21630859375,910.841796875,888.71728515625,1187.474609375,769.20703125,793.69287109375,249.9365234375,637.72802734375,1719.05517578125,255.015625,364.36962890625,625.1337890625,393.67529296875,658.0166015625,2708.09033203125,853.712890625,2004.76708984375,2879.8359375,787.97119140625,452.62548828125,356.95947265625,483.81787109375,291.30908203125,408.85400390625,291.033203125,512.5009765625,374.71533203125,834.9765625,363.90234375,276.18115234375,199.138671875,255.14013671875,300.75390625,473.57421875,830.51611328125,1459.52197265625,880.98583984375,943.9892578125,1212.81884765625,2805.4375,1048.94384765625,1890.83251953125,502.57177734375,592.765625,725.79296875,231.49951171875,1451.376953125,579.39501953125,384.67236328125,1544.81201171875,604.76220703125,1022.1337890625,527.59326171875,538.62548828125,354.96142578125,1041.1494140625,765.67236328125,454.87744140625,954.5546875,631.97314453125,865.0263671875,777.1259765625,394.01953125,446.9248046875,642.490234375,316.41455078125,129.41455078125,397.76220703125,165.26904296875,205.5703125,1416.9375,601.10595703125,1053.2802734375,422.39404296875,1151.31005859375,806.23876953125,177.8681640625,245.5126953125,749.2998046875,544.66455078125,770.38232421875,1249.4755859375,148.3515625,143.75,106.4248046875,1077.03125,602.33740234375,372.845703125,1317.8271484375,968.734375,954.2216796875,2913.388671875,557.1025390625,769.40966796875,654.890625,1351.697265625,447.93603515625,1222.36181640625,351.21142578125,1048.2666015625,1205.4619140625,413.1591796875,1625.93603515625,613.93896484375,2328.47607421875,797.64208984375,399.5478515625,373.38720703125,1680.39599609375,3876.66552734375,1092.830078125,2433.515625,2455.87841796875,3299.5380859375,2880.06396484375,2072.4833984375,1189.2626953125,892.8681640625]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":103.59,"1st Qu.":585.59,"Median":1103.1,"Mean":1151.8,"3rd Qu.":1495.6,"Max.":3410},"bucket.quantiles":{"5%":326.37822265625,"95%":2511.99519042969},"bucket.raw.data":[103.58642578125,1517.71533203125,1429.07568359375,1200.29052734375,2711.2197265625,1157.49951171875,1552.642578125,2268.49853515625,2084.2880859375,955.23486328125,579.763671875,426.32470703125,723.5556640625,638.9765625,1021.71630859375,538.49169921875,1048.6884765625,289.712890625,1291.9775390625,603.083984375,371.19140625,513.404296875,3409.98291015625,489.6455078125,1300.06591796875,1558.8369140625,1159.15966796875,1204.53564453125,824.62451171875,1580.9267578125]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":68.378,"1st Qu.":689.07,"Median":1064.4,"Mean":1179.3,"3rd Qu.":1543.3,"Max.":4643.8},"bucket.quantiles":{"5%":238.223828125,"95%":2292.41630859375},"bucket.raw.data":[708.58642578125,190.4326171875,82.6015625,68.3779296875,78.95361328125,368.39013671875,1161.15283203125,1377.982421875,1231.62841796875,980.4619140625,1788.12060546875,2071.583984375,2176.96337890625,932.24462890625,1405.80029296875,2077.01025390625,1044.19580078125,2054.298828125,1979.8076171875,1564.1689453125,1446.3271484375,1578.0087890625,688.0654296875,1199.1357421875,2805.26513671875,1989.2490234375,1827.87353515625,1192.05810546875,686.8212890625,3031.7880859375,3028.40087890625,2293.435546875,2445.19873046875,796.46728515625,1032.076171875,882.580078125,1411.64599609375,970.99169921875,975.62109375,1414.33154296875,1558.32421875,2304.5205078125,1352.205078125,2239.84521484375,2165.64892578125,1029.43701171875,1307.18017578125,2043.05419921875,2193.7412109375,1775.6513671875,1695.01025390625,1031.78564453125,584.828125,689.41015625,893.07763671875,1871.27734375,707.224609375,1458.740234375,1370.81689453125,699.3984375,793.79052734375,1028.74755859375,1916.4560546875,2290.5234375,834.302734375,2003.5947265625,1226.65576171875,828.728515625,947.9404296875,969.78564453125,818.95556640625,1343.88232421875,1595.17578125,1915.23486328125,1057.28271484375,709.49951171875,578.60986328125,478.77734375,467.2587890625,562.7568359375,1486.916015625,2076.85107421875,4643.81982421875,254.138671875,710.37646484375,1123.162109375,1234.27197265625,646.53271484375,367.53515625,269.50439453125,113.65478515625,143.6669921875,166.90478515625,394.75390625,445.54541015625,558.88037109375,279.57421875,470.818359375,1326.9541015625,321.95166015625,1258.3115234375,1471.5361328125,499.994140625,973.091796875,440.470703125,527.44384765625,617.94775390625,782.50390625,557.17724609375,714.4560546875,1387.71435546875,1256.28515625,1032.56201171875,229.654296875,503.6845703125,553.41650390625,577.10791015625,568.23095703125,752.35400390625,1629.091796875,939.69189453125,1410.62353515625,1076.0576171875,1365.2978515625,1273.3369140625,928.625,1486.45703125,760.1953125,3112.31689453125,1608.07861328125,752.28369140625,1538.279296875,997.93798828125,1810.06103515625,1602.8828125,1139.72021484375,414.7197265625,1096.16552734375,1197.0693359375,1088.1865234375,1071.55517578125,1096.9052734375,1294.560546875,1481.27587890625,836.74658203125,1039.0009765625,1746.20263671875,619.42236328125]}},"bucketscores":["bucket1",100,"bucket2",0]},"system.net.packets_out.error":{"metricName":"system.net.packets_out.error","metricType":"Network Group","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.transmit_rate":{"metricName":"system.net.transmit_rate","metricType":"Network Group","score":0,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":0,"1st Qu.":45.4,"Median":64.484,"Mean":68.091,"3rd Qu.":86.373,"Max.":183.11},"version2":{"Min.":0,"1st Qu.":39.07,"Median":52.666,"Mean":53.011,"3rd Qu.":65.293,"Max.":110}},"quantileinfo":{"version1":{"5%":28.333544921875,"95%":123.638134765625},"version2":{"5%":23.040771484375,"95%":86.54248046875}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":20.389,"1st Qu.":44.139,"Median":63.803,"Mean":65.326,"3rd Qu.":81.224,"Max.":141.25},"bucket.quantiles":{"5%":24.51005859375,"95%":111.9158203125},"bucket.raw.data":[107.267578125,99.314453125,80.90771484375,84.03564453125,40.89013671875,42.02490234375,38.16943359375,69.03173828125,23.63427734375,52.58154296875,35.4814453125,44.29296875,71.0146484375,80.98828125,79.37890625,48.1796875,47.89599609375,72.7626953125,123.6708984375,74.236328125,63.802734375,86.99169921875,38.28173828125,64.55322265625,104.31298828125,141.25,110.662109375,68.18896484375,108.6669921875,101.97509765625,79.06640625,89.99951171875,80.892578125,63.4931640625,59.498046875,65.2041015625,97.759765625,116.29296875,85.578125,66.74853515625,82.62255859375,69.99169921875,72.24267578125,115.36181640625,81.458984375,65.5126953125,68.7978515625,61.44970703125,61.31689453125,35.68359375,61.95556640625,49.046875,60.5419921875,45.12939453125,84.8310546875,53.693359375,21.60595703125,30.8701171875,24.45263671875,43.52294921875,47.40966796875,103.52392578125,103.4443359375,60.7861328125,46.48388671875,43.98583984375,71.82666015625,59.2978515625,29.70849609375,49.73193359375,77.009765625,112.453125,59.63330078125,58.01220703125,43.23974609375,65.0693359375,20.38916015625,22.23779296875,26.3330078125,24.64404296875,28.3994140625,66.3125,37.07080078125,41.52099609375,39.36279296875,54.3203125,92.1162109375]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":15.164,"1st Qu.":43.232,"Median":62.829,"Mean":66.18,"3rd Qu.":83.46,"Max.":183.11},"bucket.quantiles":{"5%":28.094921875,"95%":116.333056640625},"bucket.raw.data":[115.70654296875,118.2705078125,108.77978515625,84.9892578125,67.1650390625,64.837890625,75.34765625,106.29345703125,183.1103515625,79.4775390625,82.6689453125,109.6337890625,102.35205078125,121.76123046875,80.21240234375,36.14990234375,45.61474609375,39.04345703125,37.20703125,54.32177734375,65.669921875,69.0048828125,105.46533203125,56.1435546875,87.58740234375,73.32763671875,48.314453125,30.15869140625,32.2998046875,37.29248046875,77.50439453125,61.66259765625,29.5400390625,49.0224609375,45.28125,20.521484375,65.3232421875,57.93505859375,82.76708984375,45.97265625,35.36669921875,32.6845703125,29.974609375,47.05029296875,33.32666015625,74.68701171875,33.10107421875,30.30322265625,37.10791015625,47.18359375,89.59033203125,132.74560546875,67.91552734375,108.94970703125,90.7265625,72.2548828125,71.45849609375,68.373046875,83.33935546875,109.53466796875,141.65869140625,91.91845703125,79.09619140625,87.2607421875,42.54443359375,40.83544921875,27.03759765625,51.65625,48.91162109375,47.44677734375,84.79345703125,44.0966796875,116.6015625,67.2529296875,55.9931640625,74.7421875,80.11376953125,110.50927734375,54.673828125,87.45849609375,71.38330078125,46.16015625,53.27392578125,38.60888671875,72.431640625,28.869140625,75.65478515625,33.2607421875,73.01708984375,70.359375,46.8779296875,79.2275390625,71.42333984375,151.91259765625,90.6337890625,87.22314453125,51.5693359375,94.17626953125,82.2685546875,61.2822265625,67.037109375,57.26953125,57.681640625,53.22314453125,42.27880859375,37.57177734375,62.71435546875,56.8720703125,57.0087890625,77.64697265625,86.787109375,32.8955078125,82.52099609375,46.10302734375,55.5751953125,101.23095703125,55.85693359375,54.837890625,89.34423828125,37.33203125,43.08154296875,73.478515625,113.80224609375,107.923828125,124.79931640625,162.955078125,113.59814453125,100.818359375,78.87841796875,132.27294921875,105.71337890625,79.35498046875,104.23974609375,129.76123046875,142.36376953125,58.11572265625,113.66064453125,88.107421875,70.8330078125,82.41552734375,63.36962890625,81.78369140625,103.759765625,107.27099609375,78.1875,109.490234375,91.56494140625,103.24609375,85.19384765625,106.8544921875,73.1875,72.62353515625,83.70166015625,141.7705078125,128.38623046875,134.50244140625,80.29638671875,71.21923828125,61.6357421875,71.9287109375,70.2529296875,79.41796875,86.1552734375,98.5703125,108.1357421875,100.7119140625,95.0283203125,75.68359375,77.12646484375,65.56005859375,69.365234375,62.3291015625,52.90966796875,67.88818359375,72.3896484375,62.19873046875,50.72900390625,71.681640625,86.02880859375,36.19140625,60.837890625,39.7587890625,83.5810546875,87.57080078125,71.34912109375,58.0654296875,47.5380859375,81.849609375,27.90625,26.06591796875,47.91455078125,29.109375,35.34765625,40.8916015625,35.880859375,28.3779296875,31.16357421875,33.59716796875,36.91748046875,19.30419921875,51.84619140625,37.36865234375,33.43359375,62.8291015625,50.8515625,52.30224609375,154.35302734375,81.60595703125,93.328125,91.80712890625,77.3388671875,82.0361328125,73.36181640625,50.11376953125,52.23828125,31.54296875,72.0009765625,87.26953125,54.30908203125,61.71435546875,90.22265625,51.3330078125,49.953125,107.0166015625,53.3515625,86.03515625,102.9140625,54.84326171875,42.46533203125,34.37890625,51.71240234375,39.64013671875,33.91064453125,25.00439453125,37.453125,30.80322265625,41.14599609375,35.0283203125,23.17578125,21.9951171875,36.64306640625,35.5302734375,61.98876953125,70.16357421875,89.83544921875,86.72119140625,82.63720703125,89.7021484375,121.87353515625,74.1533203125,77.86279296875,60.72900390625,50.2607421875,55.31982421875,24.15625,65.7119140625,63.4462890625,41.79638671875,60.5107421875,58.43505859375,69.61083984375,68.94384765625,57.67626953125,43.24658203125,87.806640625,67.89892578125,43.5068359375,66.83740234375,87.25244140625,57.34130859375,65.85546875,41.79296875,21.80517578125,33.57421875,17.51513671875,19.578125,34.8525390625,34.36474609375,48.99853515625,74.17578125,67.95751953125,66.9716796875,35.55517578125,54.7578125,42.37548828125,20.82421875,27.9736328125,45.3564453125,44.84765625,45.32177734375,44.15869140625,15.1640625,17.111328125,17.2158203125,44.00927734375,33.4765625,28.94873046875,51.53857421875,37.69091796875,46.046875,87.35400390625,43.216796875,44.53857421875,39.26123046875,60.94921875,30.1611328125,46.53125,32.55712890625,52.4248046875,50.6884765625,34.19775390625,65.1142578125,42.72119140625,82.36767578125,50.51611328125,39.77099609375,34.18017578125,60.56396484375,104.5888671875,52.7919921875,77.328125,93.173828125,108.6435546875,88.9970703125,77.01904296875,110.19873046875,66.49853515625]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":17.208,"1st Qu.":34.998,"Median":49.972,"Mean":50.53,"3rd Qu.":62.609,"Max.":100.78},"bucket.quantiles":{"5%":22.990576171875,"95%":75.9057373046875},"bucket.raw.data":[21.3369140625,100.77685546875,71.44970703125,66.3359375,75.02783203125,49.6748046875,57.501953125,62.529296875,67.0234375,54.81884765625,29.3759765625,28.09375,34.7724609375,49.34326171875,50.2685546875,30.3505859375,43.6337890625,17.20751953125,47.6767578125,25.01171875,30.1943359375,35.67431640625,76.6240234375,40.70947265625,62.45166015625,70.80810546875,62.6357421875,60.97119140625,42.7021484375,50.93310546875]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":10.648,"1st Qu.":40.192,"Median":51.472,"Mean":52.782,"3rd Qu.":65.353,"Max.":105.26},"bucket.quantiles":{"5%":22.0898681640625,"95%":85.6133544921875},"bucket.raw.data":[51.11474609375,40.37939453125,23.11328125,21.83349609375,22.21142578125,64.28564453125,91.978515625,83.1943359375,85.05615234375,44.2578125,66.3427734375,75.01513671875,76.71826171875,55.23095703125,61.65576171875,90.83251953125,63.1640625,85.65625,59.74169921875,40.1259765625,61.904296875,62.75634765625,46.23291015625,57.7373046875,78.91455078125,64.609375,56.19873046875,51.10791015625,40.89208984375,80.005859375,105.26123046875,77.3212890625,76.61328125,45.79541015625,45.71826171875,44.638671875,53.2724609375,57.10791015625,44.28759765625,56.87548828125,65.560546875,70.6298828125,62.35546875,77.07373046875,74.51025390625,47.1611328125,75.109375,85.53369140625,95.5048828125,73.24169921875,45.125,48.455078125,30.93994140625,30.79638671875,31.361328125,58.10009765625,43.08154296875,61.580078125,43.47998046875,26.26611328125,36.3076171875,49.89404296875,64.90185546875,80.5380859375,47.31591796875,66.6220703125,51.3037109375,41.1279296875,52.9697265625,51.37890625,42.50830078125,56.904296875,73.45849609375,66.84814453125,37.8974609375,37.93603515625,28.5927734375,28.6474609375,30.57373046875,35.2626953125,47.32177734375,52.666015625,97.64453125,18.27490234375,30.123046875,42.48095703125,40.490234375,36.966796875,25.923828125,18.57958984375,11.8564453125,12.69677734375,10.64794921875,22.0244140625,20.41455078125,24.62841796875,25.2294921875,35.74853515625,57.81494140625,37.78955078125,45.1025390625,58.5263671875,30.12060546875,43.2734375,30.81591796875,40.21435546875,49.39501953125,47.9580078125,37.61865234375,32.97265625,55.53076171875,46.705078125,43.68505859375,25.8701171875,37.08154296875,39.068359375,47.63671875,43.6142578125,51.56494140625,69.2958984375,47.7109375,65.283203125,67.8974609375,70.841796875,60.00146484375,60.34375,56.9013671875,63.68994140625,93.5888671875,69.294921875,53.4365234375,64.45703125,55.7041015625,87.03369140625,70.07373046875,50.28076171875,42.25537109375,60.08154296875,49.33984375,53.8525390625,67.3623046875,64.35400390625,66.12060546875,70.69189453125,53.25634765625,54.234375,76.71240234375,35.5205078125]}},"bucketscores":["bucket1",0,"bucket2",0]},"system.mem.swap.in":{"metricName":"system.mem.swap.in","metricType":"Memory Group","score":0,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":5.5079,"3rd Qu.":0,"Max.":2944},"version2":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":51.862,"3rd Qu.":16,"Max.":1268}},"quantileinfo":{"version1":{"5%":0,"95%":0},"version2":{"5%":0,"95%":337.199999999999}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.73563,"3rd Qu.":0,"Max.":32},"bucket.quantiles":{"5%":0,"95%":0},"bucket.raw.data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,16,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,16,0,0,0,0,32,0,0,0,0,0,0]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.88073,"3rd Qu.":0,"Max.":128},"bucket.quantiles":{"5%":0,"95%":0},"bucket.raw.data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,16,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,128,0,0,0,0,0,0,0,0,0,0,0,16,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,64,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,32,0,0,0,0,0,0,0,32,0,0,0,0,0,0,0,0,0,0,0]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":36.385,"3rd Qu.":12,"Max.":312},"bucket.quantiles":{"5%":0,"95%":204},"bucket.raw.data":[0,0,0,0,16,0,0,0,0,70,0,224,312,0,0,0,0,0,144,100,0,0,0,0,0,80]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":43.987,"3rd Qu.":10,"Max.":544},"bucket.quantiles":{"5%":0,"95%":307.099999999999},"bucket.raw.data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,48,0,0,0,0,0,0,0,0,0,0,0,0,0,32,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,128,0,0,0,0,0,96,32,0,0,0,0,0,96,366,48,96,0,0,30,0,0,0,0,240,42,350,186,180,190,160,0,0,0,0,112,0,0,0,0,0,416,182,544,138,382,272,176,0,0,0,0,0,0,0,0,0,0,0,160,0,0,0,0,0,16,0,160,96,366,490,448,100,0,8,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,32,128,44,64,32]}},"bucketscores":["bucket1",0,"bucket2",0]},"system.mem.swap.total":{"metricName":"system.mem.swap.total","metricType":"Memory Group","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.mem.util":{"metricName":"system.mem.util","metricType":"Memory Group","score":0,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":91,"1st Qu.":92,"Median":97,"Mean":94.793,"3rd Qu.":97,"Max.":98},"version2":{"Min.":55,"1st Qu.":80,"Median":82,"Mean":79.173,"3rd Qu.":82,"Max.":83}},"quantileinfo":{"version1":{"5%":92,"95%":98},"version2":{"5%":64,"95%":82}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":92,"1st Qu.":92,"Median":97,"Mean":95.552,"3rd Qu.":98,"Max.":98},"bucket.quantiles":{"5%":92,"95%":98},"bucket.raw.data":[92,92,92,92,93,93,92,92,92,92,92,92,92,92,93,92,92,93,93,92,92,92,92,92,92,92,92,92,93,93,93,95,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,97,98,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":92,"1st Qu.":92,"Median":97,"Mean":94.942,"3rd Qu.":97,"Max.":98},"bucket.quantiles":{"5%":92,"95%":98},"bucket.raw.data":[92,92,92,92,92,92,92,92,92,92,92,92,93,93,93,93,93,93,93,93,93,92,93,93,93,92,92,92,93,92,92,92,92,92,92,92,92,92,92,92,93,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,93,92,92,92,92,93,93,92,93,93,92,93,93,93,93,93,92,92,92,92,92,92,92,92,92,92,92,93,93,93,93,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,92,93,93,93,93,93,93,93,93,93,93,93,93,94,95,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,98,97,97,97,98,98,98,98,98,98,98,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,96,97,96,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97,97]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":55,"1st Qu.":80,"Median":82,"Mean":79.308,"3rd Qu.":82,"Max.":82},"bucket.quantiles":{"5%":65.5,"95%":82},"bucket.raw.data":[55,62,76,77,79,80,80,80,81,81,81,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":59,"1st Qu.":80,"Median":82,"Mean":79.099,"3rd Qu.":82,"Max.":83},"bucket.quantiles":{"5%":65.1,"95%":82},"bucket.raw.data":[59,60,60,60,60,60,61,64,66,66,66,67,67,67,69,69,70,70,70,73,74,76,76,76,76,77,77,78,79,79,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80,81,81,81,81,81,81,81,81,81,81,81,81,81,81,81,81,81,81,81,81,81,81,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,82,83,83,82,82,82,82,82,82,82,82,82,82,83]}},"bucketscores":["bucket1",0,"bucket2",0]},"system.mem.swap.out":{"metricName":"system.mem.swap.out","metricType":"Memory Group","score":100,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":148.99,"3rd Qu.":0,"Max.":10490},"version2":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":11.996,"3rd Qu.":0,"Max.":796}},"quantileinfo":{"version1":{"5%":0,"95%":732},"version2":{"5%":0,"95%":84}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":314.53,"3rd Qu.":50,"Max.":4454},"bucket.quantiles":{"5%":0,"95%":2425.2},"bucket.raw.data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,772,0,0,0,0,0,0,0,0,536,688,128,0,0,4454,0,0,0,0,0,100,0,2316,3076,0,1894,2556,404,0,2472,0,0,0,0,1050,0,0,338,0,0,0,0,2712,0,0,522,0,0,1246,0,0,0,0,750,592,0,500,140,0,0,0,118,0,0,0,0]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":109.24,"3rd Qu.":0,"Max.":4056},"bucket.quantiles":{"5%":0,"95%":537.4},"bucket.raw.data":[0,0,0,716,0,0,0,0,0,0,0,0,0,0,0,0,0,470,0,0,670,0,0,0,0,252,96,202,0,0,0,0,158,0,536,0,0,0,0,524,1480,0,0,0,0,0,0,340,34,68,180,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,406,0,0,0,0,0,0,0,0,0,0,0,0,0,328,0,0,0,0,0,0,0,704,0,0,0,214,0,0,0,0,344,0,0,598,32,466,44,520,0,0,426,0,0,192,0,0,0,0,0,0,0,0,0,0,0,0,0,0,120,0,194,0,0,0,0,0,206,0,0,538,0,0,0,0,0,0,0,0,106,0,0,4056,534,0,0,3794,3000,0,0,0,0,0,0,0,0,0,0,0,0,2808,78,0,1968,0,676,0,0,0,4,32,0,0,0,0,0,0,0,0,0,10,0,0,0,0,0,1070,1404,166,0,0,0,0,0,0,0,258,0,0,76,0,0,0,0,0,0,0,0,0,0,430,0,192,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,24,0,0,0,0,0,0,0,0,0,0,0,40,0,260,0,0,0,720,0,0,0,0,0,1854,558,0,0,0,8,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,0,24,0,0,0,126,0,0,0,112,0,0,0,0,0,0,0,0,0,64,0,0,0,0,0,0,0,0,0,204,0,0,0,0,0,0,0,0,0,0]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":24.846,"3rd Qu.":0,"Max.":312},"bucket.quantiles":{"5%":0,"95%":156},"bucket.raw.data":[0,0,0,0,38,0,0,0,0,32,0,0,312,0,0,0,0,0,0,0,0,0,0,0,180,84]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":8.6579,"3rd Qu.":0,"Max.":146},"bucket.quantiles":{"5%":0,"95%":74.3999999999996},"bucket.raw.data":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,30,30,0,0,2,116,0,18,0,0,0,16,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,8,0,0,2,0,0,0,8,0,30,58,0,146,0,0,0,0,0,0,0,0,0,8,0,0,0,0,0,0,0,0,0,0,144,0,0,52,0,6,120,16,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,92,0,0,0,114,60,92,0,22,0,0,118,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]}},"bucketscores":["bucket1",100,"bucket2",100]},"http.responsetime":{"metricName":"http.responsetime","metricType":"response","score":100,"error":"No error","metricstats":{"statinfo":{"version1":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":5.4747,"3rd Qu.":0,"Max.":828},"version2":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":4.974,"3rd Qu.":0,"Max.":1695}},"quantileinfo":{"version1":{"5%":0,"95%":23},"version2":{"5%":0,"95%":22}}},"version1.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":10.126,"3rd Qu.":0,"Max.":366},"bucket.quantiles":{"5%":0,"95%":22.7},"bucket.raw.data":[0,0,0,0,0,2,0,0,1,0,37,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,15,0,0,0,0,0,0,0,245,0,0,3,0,0,0,0,26,0,14,6,0,0,0,0,2,12,0,0,0,0,0,0,0,0,0,0,0,366,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,137,10,0]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":2.737,"3rd Qu.":0,"Max.":283},"bucket.quantiles":{"5%":0,"95%":14.7},"bucket.raw.data":[4,0,24,0,0,0,0,0,3,0,0,0,6,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,35,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,15,0,0,7,0,0,0,0,0,0,0,20,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,283,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,29,0,0,21,0,0,0,0,0,0,1,0,2,6,0,4,0,16,0,0,0,25,0,0,0,0,0,0,0,24,14,0,0,0,0,0,0,0,21,1,8,4,0,0,0,0,0,0,11,0,0,0,0,4,0,0,0,0,0,0,0,0,0,3,0,0,7,0,0,0,10,11,0,0,0,3,0,0,0,0,0,0,27,0,0,13,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,96,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,22,15,0,3,0,0,0,0,0,0,17,0,0,60,0,0,0,0,0,6,0,0,0]}},"version2.model.char":{"bucket1":{"bucketname":"bucket1","bucket.load.range":"8-9","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":45.192,"3rd Qu.":7,"Max.":1050},"bucket.quantiles":{"5%":0,"95%":42},"bucket.raw.data":[0,0,0,0,27,15,0,0,9,0,0,0,0,1050,0,0,11,0,47,0,0,0,0,15,0,1]},"bucket2":{"bucketname":"bucket2","bucket.load.range":"9-10","bucket.stats":{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.96053,"3rd Qu.":0,"Max.":48},"bucket.quantiles":{"5%":0,"95%":3.44999999999999},"bucket.raw.data":[0,0,0,0,0,0,0,1,0,0,0,0,0,11,0,0,0,0,0,0,0,0,0,0,0,0,0,5,0,0,0,0,0,0,0,0,0,0,0,0,48,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,0,0,0,1,0,0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,47,0,0,0]}},"bucketscores":["bucket1",100,"bucket2",100]},"tomcat.processingTime_http-bio-8080":{"metricName":"tomcat.processingTime_http-bio-8080","metricType":"Tomcat","score":"NA","error":"Zero data points downloaded for this metric."},"tomcat.processingTime_Manager":{"metricName":"tomcat.processingTime_Manager","metricType":"Tomcat","score":"NA","error":"Zero data points downloaded for this metric."},"tomcat.requestCount_Manager":{"metricName":"tomcat.requestCount_Manager","metricType":"Tomcat","score":"NA","error":"Zero data points downloaded for this metric."}}}}
\.


--
-- Data for Name: svcservice; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY svcservice (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, servicename) FROM stdin;
1	n42.domain.model.SVCService	2016-07-14 15:29:51.795	2016-07-14 15:29:51.795	1	t	MySQL
2	n42.domain.model.SVCService	2016-07-14 15:29:51.795	2016-07-14 15:29:51.795	2	t	Tomcat
\.


--
-- Data for Name: svcservice_serviceversions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY svcservice_serviceversions (parent_id, value, sort_order) FROM stdin;
1	v1.0	0
1	v1.1	1
1	v1.2	3
2	v1.0	0
2	v1.1	1
2	v1.2	3
\.


--
-- Data for Name: svcservicemetricdetails; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY svcservicemetricdetails (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, metricname, metrictype, kairosaggregator) FROM stdin;
29	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.currentThreadsBusy	Tomcat	avg
15	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.HeapMemoryUsage	Tomcat	avg
14	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.Uptime	Tomcat	avg
1	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	mysql.bytes_received	MySQL	avg
2	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	mysql.bytes_sent	MySQL	avg
4	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	mysql.innodb_buffer_pool_write_requests	MySQL	avg
6	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	mysql.connections_running	MySQL	avg
8	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	mysql.connections_cached	MySQL	avg
10	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	mysql.pct_query_cache_hit_utilization	MySQL	avg
13	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	mysql.com_change_db	MySQL	avg
11	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	mysql.query_cache_hits	MySQL	avg
9	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	mysql.pct_connection_utilization	MySQL	avg
7	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	mysql.connections_connected	MySQL	avg
5	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	mysql.connections_connected	MySQL	avg
3	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	mysql.innodb_buffer_pool_reads	MySQL	avg
16	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.NonHeapMemoryUsage	Tomcat	avg
17	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.rejectedSessions	Tomcat	avg
18	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.ThreadCount	Tomcat	avg
19	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.activeSessions	Tomcat	avg
20	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.sessionCreateRate	Tomcat	avg
21	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.processingTime_http-bio-8080	Tomcat	avg
22	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.bytesSent	Tomcat	avg
23	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.bytesReceived	Tomcat	avg
24	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.processingTime_Manager	Tomcat	avg
25	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.errorCount	Tomcat	avg
26	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.maxTime	Tomcat	avg
27	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.requestCount_Manager	Tomcat	avg
28	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	tomcat.maxThreads	Tomcat	avg
\.


--
-- Data for Name: svcservicemetrics; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY svcservicemetrics (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, servicename, metricname) FROM stdin;
\.


--
-- Data for Name: svcversion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY svcversion (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, versionname, starttime, endtime, hostvalue) FROM stdin;
1	n42.domain.model.SVCVersion	2016-07-14 15:29:51.795	2016-07-14 15:29:51.795	1	t	v1.0	1468304760000	1468312020000	67.205.124.58
2	n42.domain.model.SVCVersion	2016-07-14 15:29:51.795	2016-07-14 15:29:51.795	2	t	v1.1	1468313520000	1468315320000	67.205.124.58
3	n42.domain.model.SVCVersion	2016-07-14 15:29:51.795	2016-07-14 15:29:51.795	3	t	v1.2	1468485000000	1468491300000	67.205.124.58
4	n42.domain.model.SVCVersion	2016-07-14 15:29:51.795	2016-07-14 15:29:51.795	3	t	v1.3	1468491900000	1468495980000	67.205.124.58
\.


--
-- Data for Name: versionstats; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY versionstats (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, min, firstqu, median, mean, thirdqu, max) FROM stdin;
\.


--
-- Name: action_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY action
    ADD CONSTRAINT action_pkey PRIMARY KEY (n42_id);


--
-- Name: bucketscore_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY bucketscore
    ADD CONSTRAINT bucketscore_pkey PRIMARY KEY (n42_id);


--
-- Name: canary_notificationhours_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY canary_notificationhours
    ADD CONSTRAINT canary_notificationhours_pkey PRIMARY KEY (parent_id, sort_order);


--
-- Name: canary_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY canary
    ADD CONSTRAINT canary_pkey PRIMARY KEY (n42_id);


--
-- Name: canary_watcherids_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY canary_watcherids
    ADD CONSTRAINT canary_watcherids_pkey PRIMARY KEY (parent_id, sort_order);


--
-- Name: canaryanalysis_metricscoreids_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY canaryanalysis_metricscoreids
    ADD CONSTRAINT canaryanalysis_metricscoreids_pkey PRIMARY KEY (parent_id, sort_order);


--
-- Name: canaryanalysis_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY canaryanalysis
    ADD CONSTRAINT canaryanalysis_pkey PRIMARY KEY (n42_id);


--
-- Name: casservicemetricdetails_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY casservicemetricdetails
    ADD CONSTRAINT casservicemetricdetails_pkey PRIMARY KEY (n42_id);


--
-- Name: casservicemetrics_metricsdetails_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY casservicemetrics_metricsdetails
    ADD CONSTRAINT casservicemetrics_metricsdetails_pkey PRIMARY KEY (metricsdetails_casservicemetrics, sort_order);


--
-- Name: casservicemetrics_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY casservicemetrics
    ADD CONSTRAINT casservicemetrics_pkey PRIMARY KEY (n42_id);


--
-- Name: cluster_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cluster
    ADD CONSTRAINT cluster_pkey PRIMARY KEY (n42_id);


--
-- Name: health_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY health
    ADD CONSTRAINT health_pkey PRIMARY KEY (n42_id);


--
-- Name: host_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY host
    ADD CONSTRAINT host_pkey PRIMARY KEY (n42_id);


--
-- Name: metric_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY metric
    ADD CONSTRAINT metric_pkey PRIMARY KEY (n42_id);


--
-- Name: metricscore_bucketscoreids_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY metricscore_bucketscoreids
    ADD CONSTRAINT metricscore_bucketscoreids_pkey PRIMARY KEY (parent_id, sort_order);


--
-- Name: metricscore_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY metricscore
    ADD CONSTRAINT metricscore_pkey PRIMARY KEY (n42_id);


--
-- Name: ownerwatcherdata_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY ownerwatcherdata
    ADD CONSTRAINT ownerwatcherdata_pkey PRIMARY KEY (n42_id);


--
-- Name: service_bucketids_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY service_bucketids
    ADD CONSTRAINT service_bucketids_pkey PRIMARY KEY (parent_id, sort_order);


--
-- Name: service_hostids_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY service_hostids
    ADD CONSTRAINT service_hostids_pkey PRIMARY KEY (parent_id, sort_order);


--
-- Name: service_metricids_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY service_metricids
    ADD CONSTRAINT service_metricids_pkey PRIMARY KEY (parent_id, sort_order);


--
-- Name: service_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY service
    ADD CONSTRAINT service_pkey PRIMARY KEY (n42_id);


--
-- Name: serviceversion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY serviceversion
    ADD CONSTRAINT serviceversion_pkey PRIMARY KEY (n42_id);


--
-- Name: serviceversioncharacterstic_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY serviceversioncharacterstic
    ADD CONSTRAINT serviceversioncharacterstic_pkey PRIMARY KEY (n42_id);


--
-- Name: serviceversioncharacterstic_serviceversionid_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY serviceversioncharacterstic_serviceversionid
    ADD CONSTRAINT serviceversioncharacterstic_serviceversionid_pkey PRIMARY KEY (parent_id, sort_order);


--
-- Name: status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY status
    ADD CONSTRAINT status_pkey PRIMARY KEY (n42_id);


--
-- Name: svcresult_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY svcresult
    ADD CONSTRAINT svcresult_pkey PRIMARY KEY (n42_id);


--
-- Name: svcservice_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY svcservice
    ADD CONSTRAINT svcservice_pkey PRIMARY KEY (n42_id);


--
-- Name: svcservice_serviceversions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY svcservice_serviceversions
    ADD CONSTRAINT svcservice_serviceversions_pkey PRIMARY KEY (parent_id, sort_order);


--
-- Name: svcservicemetricdetails_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY svcservicemetricdetails
    ADD CONSTRAINT svcservicemetricdetails_pkey PRIMARY KEY (n42_id);


--
-- Name: svcservicemetrics_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY svcservicemetrics
    ADD CONSTRAINT svcservicemetrics_pkey PRIMARY KEY (n42_id);


--
-- Name: svcversion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY svcversion
    ADD CONSTRAINT svcversion_pkey PRIMARY KEY (n42_id);


--
-- Name: versionstats_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY versionstats
    ADD CONSTRAINT versionstats_pkey PRIMARY KEY (n42_id);


--
-- Name: fk138cc88680395f38; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY service_hostids
    ADD CONSTRAINT fk138cc88680395f38 FOREIGN KEY (parent_id) REFERENCES service(n42_id);


--
-- Name: fk1558351d78ccaf55; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY canary_notificationhours
    ADD CONSTRAINT fk1558351d78ccaf55 FOREIGN KEY (parent_id) REFERENCES canary(n42_id);


--
-- Name: fk6b8325fe80395f38; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY service_metricids
    ADD CONSTRAINT fk6b8325fe80395f38 FOREIGN KEY (parent_id) REFERENCES service(n42_id);


--
-- Name: fk6cf6edcd6bc4edc5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY metricscore_bucketscoreids
    ADD CONSTRAINT fk6cf6edcd6bc4edc5 FOREIGN KEY (parent_id) REFERENCES metricscore(n42_id);


--
-- Name: fk998356e296fa9915; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY metricscore
    ADD CONSTRAINT fk998356e296fa9915 FOREIGN KEY (version2stats_versionstats) REFERENCES versionstats(n42_id);


--
-- Name: fk998356e2c9503754; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY metricscore
    ADD CONSTRAINT fk998356e2c9503754 FOREIGN KEY (version1stats_versionstats) REFERENCES versionstats(n42_id);


--
-- Name: fkab2f7e3678523ec; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY action
    ADD CONSTRAINT fkab2f7e3678523ec FOREIGN KEY (actionsforunhealthycanary_canary) REFERENCES canary(n42_id);


--
-- Name: fkae7a28982936b5e8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY canary
    ADD CONSTRAINT fkae7a28982936b5e8 FOREIGN KEY (health_health) REFERENCES health(n42_id);


--
-- Name: fkae7a2898b0af2ebe; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY canary
    ADD CONSTRAINT fkae7a2898b0af2ebe FOREIGN KEY (status_status) REFERENCES status(n42_id);


--
-- Name: fkb00f5c5b3187ded9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY casservicemetrics_metricsdetails
    ADD CONSTRAINT fkb00f5c5b3187ded9 FOREIGN KEY (metricsdetails_casservicemetrics) REFERENCES casservicemetrics(n42_id);


--
-- Name: fkb00f5c5b99882037; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY casservicemetrics_metricsdetails
    ADD CONSTRAINT fkb00f5c5b99882037 FOREIGN KEY (casservicemetricdetails) REFERENCES casservicemetricdetails(n42_id);


--
-- Name: fkb0cf90c1f846f151; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY canaryanalysis_metricscoreids
    ADD CONSTRAINT fkb0cf90c1f846f151 FOREIGN KEY (parent_id) REFERENCES canaryanalysis(n42_id);


--
-- Name: fkb5dfe866b8d20072; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY svcservice_serviceversions
    ADD CONSTRAINT fkb5dfe866b8d20072 FOREIGN KEY (parent_id) REFERENCES svcservice(n42_id);


--
-- Name: fkd41a731cff570744; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY serviceversioncharacterstic_serviceversionid
    ADD CONSTRAINT fkd41a731cff570744 FOREIGN KEY (parent_id) REFERENCES serviceversioncharacterstic(n42_id);


--
-- Name: fkefd4f18378ccaf55; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY canary_watcherids
    ADD CONSTRAINT fkefd4f18378ccaf55 FOREIGN KEY (parent_id) REFERENCES canary(n42_id);


--
-- Name: fkfb38af8480395f38; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY service_bucketids
    ADD CONSTRAINT fkfb38af8480395f38 FOREIGN KEY (parent_id) REFERENCES service(n42_id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

