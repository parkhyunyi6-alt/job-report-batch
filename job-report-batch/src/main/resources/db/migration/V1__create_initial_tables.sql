CREATE TABLE job_posting (
    id UUID PRIMARY KEY,
    source VARCHAR(50) NOT NULL,
    external_id VARCHAR(255),
    title VARCHAR(500) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    career_min INTEGER,
    career_max INTEGER,
    employment_type VARCHAR(50),
    tech_stack TEXT,
    description TEXT,
    url TEXT,
    posted_at TIMESTAMP,
    deadline_at TIMESTAMP,
    collected_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT uq_job_posting_source_external_id UNIQUE (source, external_id)
);

CREATE UNIQUE INDEX uq_job_posting_source_url
    ON job_posting (source, url)
    WHERE url IS NOT NULL;

CREATE TABLE job_match_result (
    id UUID PRIMARY KEY,
    job_posting_id UUID NOT NULL REFERENCES job_posting (id),
    total_score INTEGER NOT NULL,
    tech_score INTEGER NOT NULL,
    career_score INTEGER NOT NULL,
    location_score INTEGER NOT NULL,
    company_score INTEGER NOT NULL,
    growth_score INTEGER NOT NULL,
    reason TEXT,
    risk TEXT,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE daily_report (
    id UUID PRIMARY KEY,
    report_date DATE NOT NULL,
    title VARCHAR(255) NOT NULL,
    summary TEXT,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE daily_report_item (
    id UUID PRIMARY KEY,
    daily_report_id UUID NOT NULL REFERENCES daily_report (id),
    job_posting_id UUID NOT NULL REFERENCES job_posting (id),
    job_match_result_id UUID NOT NULL REFERENCES job_match_result (id),
    rank_no INTEGER NOT NULL,
    recommendation_level VARCHAR(50) NOT NULL,
    comment TEXT,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE user_job_preference (
    id UUID PRIMARY KEY,
    preferred_locations TEXT,
    preferred_tech_stack TEXT,
    preferred_domains TEXT,
    excluded_keywords TEXT,
    min_career_years INTEGER,
    max_career_years INTEGER,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
