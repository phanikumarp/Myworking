--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
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
10	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.net.packets_in.error	system	avg
17	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.cpu.util	system	avg
19	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	mem.used	mem	avg
20	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	mem.usage.percent	mem	avg
14	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	proc.meminfo.memtotal	proc	avg
13	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	proc.cpu.wait.io	proc	avg
15	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	proc.meminfo.memfree	proc	avg
1	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993237	t	mysql.bytes_received	mysql	avg
3	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	mysql.volume_reads	mysql	avg
2	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	mysql.bytes_sent	mysql	avg
6	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	mysql.qcache_free_memory	mysql	avg
5	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	mysql.volume_writes	mysql	avg
18	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	proc.cpu.util	proc	avg
11	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	http.responsetime	http	avg
12	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	http.requests	http	avg
16	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	http.bytes_read	http	avg
8	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.cpu.iowait	system	avg
7	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.cpu.user	system	avg
9	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	system.cpu.system	system	avg
4	n42.domain.model.CasServicesMetricDetails	2016-05-24 18:38:36.283	2016-05-24 18:38:36.283	389993238	t	mysql.uptime	mysql	avg
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
8	n42.domain.model.SVCResult	2016-07-14 15:29:51.795	2016-07-14 15:29:51.795	736977045	t	123	\N	\N	\N	{"comparison_output":{"serviceName":"n42","comparisionScore":33.3333333333333,"results":{"http.responsetime":{"metricName":"http.responsetime","metricType":"response","score":50,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":0,"Mean":6.22642624889152,"3rd Qu.":0,"Max.":828},{"Min.":0,"1st Qu.":0,"Median":0,"Mean":4.35084745762712,"3rd Qu.":0,"Max.":1695}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":18.3},"set6":{"5%":0,"95%":13.65}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":51.9},"set6":{"5%":0,"95%":7}},"loadRange":["8-9","9-10"],"error":"No error"}},"http.bytes_read":{"metricName":"http.bytes_read","metricType":"response","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.io.numprocesswaiting":{"metricName":"system.io.numprocesswaiting","metricType":"cpu","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.mem.swap.in":{"metricName":"system.mem.swap.in","metricType":"memory","score":0,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":0,"Mean":5.76863181312569,"3rd Qu.":0,"Max.":2944},{"Min.":0,"1st Qu.":0,"Median":0,"Mean":45.9421579532814,"3rd Qu.":16,"Max.":1362}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":0},"set6":{"5%":0,"95%":0}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":214.4},"set6":{"5%":0,"95%":404.6}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.mem.swap.out":{"metricName":"system.mem.swap.out","metricType":"memory","score":50,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":0,"Mean":175.016685205784,"3rd Qu.":0,"Max.":10490},{"Min.":0,"1st Qu.":0,"Median":0,"Mean":16.9321468298109,"3rd Qu.":0,"Max.":796}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":2497.2},"set6":{"5%":0,"95%":644.799999999998}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":165.6},"set6":{"5%":0,"95%":121.3}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.cpu.numprocesswaiting":{"metricName":"system.cpu.numprocesswaiting","metricType":"cpu","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.cpu.iowait":{"metricName":"system.cpu.iowait","metricType":"cpu","score":0,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.472747497219132,"3rd Qu.":0,"Max.":20},{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.221357063403782,"3rd Qu.":0,"Max.":33}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":5.59999999999999},"set6":{"5%":0,"95%":2}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":0.0999999999999943},"set6":{"5%":0,"95%":1}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.disk.blocks.write":{"metricName":"system.disk.blocks.write","metricType":"disk","score":100,"error":"No error","stats":[{"Min.":0,"1st Qu.":32,"Median":324,"Mean":1690.72525027809,"3rd Qu.":1484,"Max.":53780},{"Min.":0,"1st Qu.":26,"Median":336,"Mean":1750.94327030033,"3rd Qu.":1395,"Max.":99176}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":9244.99999999999},"set6":{"5%":0,"95%":5293.9}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":6265.79999999992},"set6":{"5%":0,"95%":7211.50000000001}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.mem.swap.total":{"metricName":"system.mem.swap.total","metricType":"memory","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.bytes_send":{"metricName":"system.net.bytes_send","metricType":"network","score":0,"error":"No error","stats":[{"Min.":0,"1st Qu.":50528.5,"Median":70204.25,"Mean":73208.1518847007,"3rd Qu.":91572.125,"Max.":187505},{"Min.":0,"1st Qu.":43456,"Median":55200.25,"Mean":55938.1044444444,"3rd Qu.":67530.875,"Max.":228713}],"version1.model.char":{"quant":{"set5":{"5%":31254.15,"95%":116045.55},"set6":{"5%":30759.975,"95%":124758.25}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":25556.8,"95%":85033.3},"set6":{"5%":28294.625,"95%":87211.75}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.net.bytes_rcvd":{"metricName":"system.net.bytes_rcvd","metricType":"network","score":50,"error":"No error","stats":[{"Min.":85996,"1st Qu.":564048.75,"Median":945151,"Mean":1125123.67371938,"3rd Qu.":1490173.375,"Max.":5016321.5},{"Min.":106324,"1st Qu.":695280,"Median":1039870,"Mean":1180989.53837597,"3rd Qu.":1479299.75,"Max.":5187761}],"version1.model.char":{"quant":{"set5":{"5%":343554.45,"95%":2347653.5},"set6":{"5%":270151.975,"95%":2576331.75}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":328943,"95%":2719473.2},"set6":{"5%":376195.275,"95%":2389123.1}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.net.packets_in.error":{"metricName":"system.net.packets_in.error","metricType":"network","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.packets_out.drops":{"metricName":"system.net.packets_out.drops","metricType":"network","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.received_rate":{"metricName":"system.net.received_rate","metricType":"network","score":50,"error":"No error","stats":[{"Min.":83.98046875,"1st Qu.":550.828857421875,"Median":922.9990234375,"Mean":1098.75358761658,"3rd Qu.":1455.24743652344,"Max.":4898.75146484375},{"Min.":103.83203125,"1st Qu.":678.984375,"Median":1015.498046875,"Mean":1153.31009607029,"3rd Qu.":1444.62866210938,"Max.":5066.1728515625}],"version1.model.char":{"quant":{"set5":{"5%":335.502392578125,"95%":2292.63037109375},"set6":{"5%":263.820288085937,"95%":2515.94897460937}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":321.2333984375,"95%":2655.735546875},"set6":{"5%":367.378198242188,"95%":2333.12802734375}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.net.packets_in.drops":{"metricName":"system.net.packets_in.drops","metricType":"network","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.packets_out.error":{"metricName":"system.net.packets_out.error","metricType":"network","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.cpu.util":{"metricName":"system.cpu.util","metricType":"cpu","score":50,"error":"No error","stats":[{"Min.":5,"1st Qu.":40,"Median":69,"Mean":65.2413793103448,"3rd Qu.":93,"Max.":97},{"Min.":8,"1st Qu.":63,"Median":87,"Mean":77.0756395995551,"3rd Qu.":94,"Max.":98}],"version1.model.char":{"quant":{"set5":{"5%":25.7,"95%":96},"set6":{"5%":18.35,"95%":96}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":42.8,"95%":96},"set6":{"5%":35.95,"95%":96}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.disk.blocks.read":{"metricName":"system.disk.blocks.read","metricType":"disk","score":50,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":6,"Mean":331.414905450501,"3rd Qu.":143,"Max.":23272},{"Min.":0,"1st Qu.":0,"Median":4,"Mean":134.55617352614,"3rd Qu.":96,"Max.":14364}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":2739.8},"set6":{"5%":0,"95%":1260.4}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":325.8},"set6":{"5%":0,"95%":564.3}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.mem.util":{"metricName":"system.mem.util","metricType":"memory","score":0,"error":"No error","stats":[{"Min.":92,"1st Qu.":92,"Median":93,"Mean":94.5650723025584,"3rd Qu.":97,"Max.":98},{"Min.":80,"1st Qu.":82,"Median":83,"Mean":82.6095661846496,"3rd Qu.":83,"Max.":84}],"version1.model.char":{"quant":{"set5":{"5%":92,"95%":98},"set6":{"5%":92,"95%":98}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":81,"95%":84},"set6":{"5%":81,"95%":84}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.net.transmit_rate":{"metricName":"system.net.transmit_rate","metricType":"network","score":0,"error":"No error","stats":[{"Min.":14.5869140625,"1st Qu.":49.4271240234375,"Median":68.558837890625,"Mean":71.5388803460816,"3rd Qu.":89.2427978515625,"Max.":183.1103515625},{"Min.":0,"1st Qu.":42.4375,"Median":53.906494140625,"Mean":54.5640842013889,"3rd Qu.":66.012939453125,"Max.":122.6259765625}],"version1.model.char":{"quant":{"set5":{"5%":30.521630859375,"95%":113.325732421875},"set6":{"5%":30.0390380859375,"95%":121.834228515625}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":24.9578125,"95%":83.04033203125},"set6":{"5%":27.6314697265625,"95%":85.167724609375}},"loadRange":["8-9","9-10"],"error":"No error"}}}}}
10	n42.domain.model.SVCResult	2016-07-14 15:29:51.795	2016-07-14 15:29:51.795	736977047	t	124	Tomcat	v2.1	v2.2	{"comparison_output":{"serviceName":"n42","comparisionScore":99.99,"results":{"http.responsetime":{"metricName":"http.responsetime","metricType":"response","score":50,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":0,"Mean":6.22642624889152,"3rd Qu.":0,"Max.":828},{"Min.":0,"1st Qu.":0,"Median":0,"Mean":4.35084745762712,"3rd Qu.":0,"Max.":1695}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":18.3},"set6":{"5%":0,"95%":13.65}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":51.9},"set6":{"5%":0,"95%":7}},"loadRange":["8-9","9-10"],"error":"No error"}},"http.bytes_read":{"metricName":"http.bytes_read","metricType":"response","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.io.numprocesswaiting":{"metricName":"system.io.numprocesswaiting","metricType":"cpu","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.mem.swap.in":{"metricName":"system.mem.swap.in","metricType":"memory","score":0,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":0,"Mean":5.76863181312569,"3rd Qu.":0,"Max.":2944},{"Min.":0,"1st Qu.":0,"Median":0,"Mean":45.9421579532814,"3rd Qu.":16,"Max.":1362}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":0},"set6":{"5%":0,"95%":0}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":214.4},"set6":{"5%":0,"95%":404.6}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.mem.swap.out":{"metricName":"system.mem.swap.out","metricType":"memory","score":50,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":0,"Mean":175.016685205784,"3rd Qu.":0,"Max.":10490},{"Min.":0,"1st Qu.":0,"Median":0,"Mean":16.9321468298109,"3rd Qu.":0,"Max.":796}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":2497.2},"set6":{"5%":0,"95%":644.799999999998}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":165.6},"set6":{"5%":0,"95%":121.3}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.cpu.numprocesswaiting":{"metricName":"system.cpu.numprocesswaiting","metricType":"cpu","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.cpu.iowait":{"metricName":"system.cpu.iowait","metricType":"cpu","score":0,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.472747497219132,"3rd Qu.":0,"Max.":20},{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.221357063403782,"3rd Qu.":0,"Max.":33}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":5.59999999999999},"set6":{"5%":0,"95%":2}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":0.0999999999999943},"set6":{"5%":0,"95%":1}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.disk.blocks.write":{"metricName":"system.disk.blocks.write","metricType":"disk","score":100,"error":"No error","stats":[{"Min.":0,"1st Qu.":32,"Median":324,"Mean":1690.72525027809,"3rd Qu.":1484,"Max.":53780},{"Min.":0,"1st Qu.":26,"Median":336,"Mean":1750.94327030033,"3rd Qu.":1395,"Max.":99176}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":9244.99999999999},"set6":{"5%":0,"95%":5293.9}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":6265.79999999992},"set6":{"5%":0,"95%":7211.50000000001}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.mem.swap.total":{"metricName":"system.mem.swap.total","metricType":"memory","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.bytes_send":{"metricName":"system.net.bytes_send","metricType":"network","score":0,"error":"No error","stats":[{"Min.":0,"1st Qu.":50528.5,"Median":70204.25,"Mean":73208.1518847007,"3rd Qu.":91572.125,"Max.":187505},{"Min.":0,"1st Qu.":43456,"Median":55200.25,"Mean":55938.1044444444,"3rd Qu.":67530.875,"Max.":228713}],"version1.model.char":{"quant":{"set5":{"5%":31254.15,"95%":116045.55},"set6":{"5%":30759.975,"95%":124758.25}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":25556.8,"95%":85033.3},"set6":{"5%":28294.625,"95%":87211.75}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.net.bytes_rcvd":{"metricName":"system.net.bytes_rcvd","metricType":"network","score":50,"error":"No error","stats":[{"Min.":85996,"1st Qu.":564048.75,"Median":945151,"Mean":1125123.67371938,"3rd Qu.":1490173.375,"Max.":5016321.5},{"Min.":106324,"1st Qu.":695280,"Median":1039870,"Mean":1180989.53837597,"3rd Qu.":1479299.75,"Max.":5187761}],"version1.model.char":{"quant":{"set5":{"5%":343554.45,"95%":2347653.5},"set6":{"5%":270151.975,"95%":2576331.75}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":328943,"95%":2719473.2},"set6":{"5%":376195.275,"95%":2389123.1}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.net.packets_in.error":{"metricName":"system.net.packets_in.error","metricType":"network","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.packets_out.drops":{"metricName":"system.net.packets_out.drops","metricType":"network","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.received_rate":{"metricName":"system.net.received_rate","metricType":"network","score":50,"error":"No error","stats":[{"Min.":83.98046875,"1st Qu.":550.828857421875,"Median":922.9990234375,"Mean":1098.75358761658,"3rd Qu.":1455.24743652344,"Max.":4898.75146484375},{"Min.":103.83203125,"1st Qu.":678.984375,"Median":1015.498046875,"Mean":1153.31009607029,"3rd Qu.":1444.62866210938,"Max.":5066.1728515625}],"version1.model.char":{"quant":{"set5":{"5%":335.502392578125,"95%":2292.63037109375},"set6":{"5%":263.820288085937,"95%":2515.94897460937}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":321.2333984375,"95%":2655.735546875},"set6":{"5%":367.378198242188,"95%":2333.12802734375}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.net.packets_in.drops":{"metricName":"system.net.packets_in.drops","metricType":"network","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.packets_out.error":{"metricName":"system.net.packets_out.error","metricType":"network","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.cpu.util":{"metricName":"system.cpu.util","metricType":"cpu","score":50,"error":"No error","stats":[{"Min.":5,"1st Qu.":40,"Median":69,"Mean":65.2413793103448,"3rd Qu.":93,"Max.":97},{"Min.":8,"1st Qu.":63,"Median":87,"Mean":77.0756395995551,"3rd Qu.":94,"Max.":98}],"version1.model.char":{"quant":{"set5":{"5%":25.7,"95%":96},"set6":{"5%":18.35,"95%":96}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":42.8,"95%":96},"set6":{"5%":35.95,"95%":96}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.disk.blocks.read":{"metricName":"system.disk.blocks.read","metricType":"disk","score":50,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":6,"Mean":331.414905450501,"3rd Qu.":143,"Max.":23272},{"Min.":0,"1st Qu.":0,"Median":4,"Mean":134.55617352614,"3rd Qu.":96,"Max.":14364}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":2739.8},"set6":{"5%":0,"95%":1260.4}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":325.8},"set6":{"5%":0,"95%":564.3}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.mem.util":{"metricName":"system.mem.util","metricType":"memory","score":0,"error":"No error","stats":[{"Min.":92,"1st Qu.":92,"Median":93,"Mean":94.5650723025584,"3rd Qu.":97,"Max.":98},{"Min.":80,"1st Qu.":82,"Median":83,"Mean":82.6095661846496,"3rd Qu.":83,"Max.":84}],"version1.model.char":{"quant":{"set5":{"5%":92,"95%":98},"set6":{"5%":92,"95%":98}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":81,"95%":84},"set6":{"5%":81,"95%":84}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.net.transmit_rate":{"metricName":"system.net.transmit_rate","metricType":"network","score":0,"error":"No error","stats":[{"Min.":14.5869140625,"1st Qu.":49.4271240234375,"Median":68.558837890625,"Mean":71.5388803460816,"3rd Qu.":89.2427978515625,"Max.":183.1103515625},{"Min.":0,"1st Qu.":42.4375,"Median":53.906494140625,"Mean":54.5640842013889,"3rd Qu.":66.012939453125,"Max.":122.6259765625}],"version1.model.char":{"quant":{"set5":{"5%":30.521630859375,"95%":113.325732421875},"set6":{"5%":30.0390380859375,"95%":121.834228515625}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":24.9578125,"95%":83.04033203125},"set6":{"5%":27.6314697265625,"95%":85.167724609375}},"loadRange":["8-9","9-10"],"error":"No error"}}}}}
9	n42.domain.model.SVCResult	2016-07-14 15:29:51.795	2016-07-14 15:29:51.795	736977046	t	124	Tomcat	v2.0	v2.1	{"comparison_output":{"serviceName":"n42","comparisionScore":66.66,"results":{"http.responsetime":{"metricName":"http.responsetime","metricType":"response","score":50,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":0,"Mean":6.22642624889152,"3rd Qu.":0,"Max.":828},{"Min.":0,"1st Qu.":0,"Median":0,"Mean":4.35084745762712,"3rd Qu.":0,"Max.":1695}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":18.3},"set6":{"5%":0,"95%":13.65}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":51.9},"set6":{"5%":0,"95%":7}},"loadRange":["8-9","9-10"],"error":"No error"}},"http.bytes_read":{"metricName":"http.bytes_read","metricType":"response","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.io.numprocesswaiting":{"metricName":"system.io.numprocesswaiting","metricType":"cpu","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.mem.swap.in":{"metricName":"system.mem.swap.in","metricType":"memory","score":0,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":0,"Mean":5.76863181312569,"3rd Qu.":0,"Max.":2944},{"Min.":0,"1st Qu.":0,"Median":0,"Mean":45.9421579532814,"3rd Qu.":16,"Max.":1362}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":0},"set6":{"5%":0,"95%":0}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":214.4},"set6":{"5%":0,"95%":404.6}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.mem.swap.out":{"metricName":"system.mem.swap.out","metricType":"memory","score":50,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":0,"Mean":175.016685205784,"3rd Qu.":0,"Max.":10490},{"Min.":0,"1st Qu.":0,"Median":0,"Mean":16.9321468298109,"3rd Qu.":0,"Max.":796}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":2497.2},"set6":{"5%":0,"95%":644.799999999998}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":165.6},"set6":{"5%":0,"95%":121.3}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.cpu.numprocesswaiting":{"metricName":"system.cpu.numprocesswaiting","metricType":"cpu","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.cpu.iowait":{"metricName":"system.cpu.iowait","metricType":"cpu","score":0,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.472747497219132,"3rd Qu.":0,"Max.":20},{"Min.":0,"1st Qu.":0,"Median":0,"Mean":0.221357063403782,"3rd Qu.":0,"Max.":33}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":5.59999999999999},"set6":{"5%":0,"95%":2}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":0.0999999999999943},"set6":{"5%":0,"95%":1}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.disk.blocks.write":{"metricName":"system.disk.blocks.write","metricType":"disk","score":100,"error":"No error","stats":[{"Min.":0,"1st Qu.":32,"Median":324,"Mean":1690.72525027809,"3rd Qu.":1484,"Max.":53780},{"Min.":0,"1st Qu.":26,"Median":336,"Mean":1750.94327030033,"3rd Qu.":1395,"Max.":99176}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":9244.99999999999},"set6":{"5%":0,"95%":5293.9}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":6265.79999999992},"set6":{"5%":0,"95%":7211.50000000001}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.mem.swap.total":{"metricName":"system.mem.swap.total","metricType":"memory","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.bytes_send":{"metricName":"system.net.bytes_send","metricType":"network","score":0,"error":"No error","stats":[{"Min.":0,"1st Qu.":50528.5,"Median":70204.25,"Mean":73208.1518847007,"3rd Qu.":91572.125,"Max.":187505},{"Min.":0,"1st Qu.":43456,"Median":55200.25,"Mean":55938.1044444444,"3rd Qu.":67530.875,"Max.":228713}],"version1.model.char":{"quant":{"set5":{"5%":31254.15,"95%":116045.55},"set6":{"5%":30759.975,"95%":124758.25}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":25556.8,"95%":85033.3},"set6":{"5%":28294.625,"95%":87211.75}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.net.bytes_rcvd":{"metricName":"system.net.bytes_rcvd","metricType":"network","score":50,"error":"No error","stats":[{"Min.":85996,"1st Qu.":564048.75,"Median":945151,"Mean":1125123.67371938,"3rd Qu.":1490173.375,"Max.":5016321.5},{"Min.":106324,"1st Qu.":695280,"Median":1039870,"Mean":1180989.53837597,"3rd Qu.":1479299.75,"Max.":5187761}],"version1.model.char":{"quant":{"set5":{"5%":343554.45,"95%":2347653.5},"set6":{"5%":270151.975,"95%":2576331.75}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":328943,"95%":2719473.2},"set6":{"5%":376195.275,"95%":2389123.1}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.net.packets_in.error":{"metricName":"system.net.packets_in.error","metricType":"network","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.packets_out.drops":{"metricName":"system.net.packets_out.drops","metricType":"network","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.received_rate":{"metricName":"system.net.received_rate","metricType":"network","score":50,"error":"No error","stats":[{"Min.":83.98046875,"1st Qu.":550.828857421875,"Median":922.9990234375,"Mean":1098.75358761658,"3rd Qu.":1455.24743652344,"Max.":4898.75146484375},{"Min.":103.83203125,"1st Qu.":678.984375,"Median":1015.498046875,"Mean":1153.31009607029,"3rd Qu.":1444.62866210938,"Max.":5066.1728515625}],"version1.model.char":{"quant":{"set5":{"5%":335.502392578125,"95%":2292.63037109375},"set6":{"5%":263.820288085937,"95%":2515.94897460937}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":321.2333984375,"95%":2655.735546875},"set6":{"5%":367.378198242188,"95%":2333.12802734375}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.net.packets_in.drops":{"metricName":"system.net.packets_in.drops","metricType":"network","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.net.packets_out.error":{"metricName":"system.net.packets_out.error","metricType":"network","score":"NA","error":"Sets have insufficient datapoints or zero standard deviation."},"system.cpu.util":{"metricName":"system.cpu.util","metricType":"cpu","score":50,"error":"No error","stats":[{"Min.":5,"1st Qu.":40,"Median":69,"Mean":65.2413793103448,"3rd Qu.":93,"Max.":97},{"Min.":8,"1st Qu.":63,"Median":87,"Mean":77.0756395995551,"3rd Qu.":94,"Max.":98}],"version1.model.char":{"quant":{"set5":{"5%":25.7,"95%":96},"set6":{"5%":18.35,"95%":96}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":42.8,"95%":96},"set6":{"5%":35.95,"95%":96}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.disk.blocks.read":{"metricName":"system.disk.blocks.read","metricType":"disk","score":50,"error":"No error","stats":[{"Min.":0,"1st Qu.":0,"Median":6,"Mean":331.414905450501,"3rd Qu.":143,"Max.":23272},{"Min.":0,"1st Qu.":0,"Median":4,"Mean":134.55617352614,"3rd Qu.":96,"Max.":14364}],"version1.model.char":{"quant":{"set5":{"5%":0,"95%":2739.8},"set6":{"5%":0,"95%":1260.4}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":0,"95%":325.8},"set6":{"5%":0,"95%":564.3}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.mem.util":{"metricName":"system.mem.util","metricType":"memory","score":0,"error":"No error","stats":[{"Min.":92,"1st Qu.":92,"Median":93,"Mean":94.5650723025584,"3rd Qu.":97,"Max.":98},{"Min.":80,"1st Qu.":82,"Median":83,"Mean":82.6095661846496,"3rd Qu.":83,"Max.":84}],"version1.model.char":{"quant":{"set5":{"5%":92,"95%":98},"set6":{"5%":92,"95%":98}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":81,"95%":84},"set6":{"5%":81,"95%":84}},"loadRange":["8-9","9-10"],"error":"No error"}},"system.net.transmit_rate":{"metricName":"system.net.transmit_rate","metricType":"network","score":0,"error":"No error","stats":[{"Min.":14.5869140625,"1st Qu.":49.4271240234375,"Median":68.558837890625,"Mean":71.5388803460816,"3rd Qu.":89.2427978515625,"Max.":183.1103515625},{"Min.":0,"1st Qu.":42.4375,"Median":53.906494140625,"Mean":54.5640842013889,"3rd Qu.":66.012939453125,"Max.":122.6259765625}],"version1.model.char":{"quant":{"set5":{"5%":30.521630859375,"95%":113.325732421875},"set6":{"5%":30.0390380859375,"95%":121.834228515625}},"loadRange":["8-9","9-10"],"error":"No error"},"version2.model.char":{"quant":{"set5":{"5%":24.9578125,"95%":83.04033203125},"set6":{"5%":27.6314697265625,"95%":85.167724609375}},"loadRange":["8-9","9-10"],"error":"No error"}}}}}
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
2	v2.0	0
2	v2.1	1
2	v2.2	2
1	v1.2	2
\.


--
-- Data for Name: svcversion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY svcversion (n42_id, type, n42createddate, n42lastupdateddate, n42hash, active, versionname, starttime, endtime, hostvalue) FROM stdin;
1	n42.domain.model.SVCVersion	2016-07-14 15:29:51.795	2016-07-14 15:29:51.795	1	t	v1.0	\N	\N	67.205.124.58
2	n42.domain.model.SVCVersion	2016-07-14 15:29:51.795	2016-07-14 15:29:51.795	2	t	v1.1	\N	\N	67.205.124.58
3	n42.domain.model.SVCVersion	2016-07-14 15:29:51.795	2016-07-14 15:29:51.795	3	t	v1.2	\N	\N	67.205.124.58
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

