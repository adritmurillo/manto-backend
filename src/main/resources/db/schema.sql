-- public.links definition

-- Drop table

-- DROP TABLE public.links;

CREATE TABLE public.links (
	link_id uuid NOT NULL,
	code_created_at timestamp(6) NOT NULL,
	code_expires_at timestamp(6) NOT NULL,
	confirmed_at timestamp(6) NULL,
	connection_code varchar(6) NOT NULL,
	link_created_at timestamp(6) NOT NULL,
	host_id uuid NOT NULL,
	protected_id uuid NOT NULL,
	link_status varchar(20) NOT NULL,
	link_updated_at timestamp(6) NOT NULL,
	CONSTRAINT links_pkey PRIMARY KEY (link_id),
	CONSTRAINT links_status_check CHECK (((link_status)::text = ANY ((ARRAY['PENDING'::character varying, 'ACTIVE'::character varying, 'REJECTED'::character varying, 'CANCELLED'::character varying])::text[]))),
	CONSTRAINT uk_link_host_protected_active UNIQUE (host_id, protected_id, link_status)
);


-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users (
	user_id uuid NOT NULL,
	active bool NOT NULL,
	user_created_at timestamp(6) NOT NULL,
	email varchar(255) NOT NULL,
	user_name varchar(100) NOT NULL,
	phone varchar(20) NOT NULL,
	CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (user_id)
);


-- public.alerts definition

-- Drop table

-- DROP TABLE public.alerts;

CREATE TABLE public.alerts (
	alert_id uuid NOT NULL,
	alert_created_at timestamp(6) NOT NULL,
	link_id uuid NOT NULL,
	protected_user_id uuid NOT NULL,
	reason varchar(500) NULL,
	resolution_note varchar(500) NULL,
	resolved_at timestamp(6) NULL,
	resolved_by_user_id uuid NULL,
	alert_status varchar(20) NOT NULL,
	suspicious_url varchar(2048) NOT NULL,
	CONSTRAINT alerts_pkey PRIMARY KEY (id),
	CONSTRAINT alerts_status_check CHECK (((alert_status)::text = ANY ((ARRAY['PENDING'::character varying, 'RESOLVED_SAFE'::character varying, 'RESOLVED_BLOCKED'::character varying])::text[]))),
	CONSTRAINT fk70n2dhnl5rvxeca9he7ej2saw FOREIGN KEY (link_id) REFERENCES public.links(link_id),
	CONSTRAINT fk8bvp8gqr0stgd6osbl8g5x96p FOREIGN KEY (resolved_by_user_id) REFERENCES public.users(user_id),
	CONSTRAINT fkm6wras2q4cghl22scayoues7l FOREIGN KEY (protected_user_id) REFERENCES public.users(user_id)
);
CREATE INDEX idx_alert_link_id ON public.alerts USING btree (link_id);
CREATE INDEX idx_alert_protected_user ON public.alerts USING btree (protected_user_id);
CREATE INDEX idx_alert_status ON public.alerts USING btree (alert_status);


-- public.device_tokens definition

-- Drop table

-- DROP TABLE public.device_tokens;

CREATE TABLE public.device_tokens (
	device_token_id uuid NOT NULL,
    device_token_created_at timestamp(6) NOT NULL,
	platform varchar(32) NOT NULL,
	device_token varchar(512) NOT NULL,
    device_token_updated_at timestamp(6) NOT NULL,
	user_id uuid NOT NULL,
	CONSTRAINT device_tokens_pkey PRIMARY KEY (device_token_id),
	CONSTRAINT idx_device_token_token UNIQUE (device_token),
	CONSTRAINT fkhc7d11bnr8x9gs5biohdhnx1c FOREIGN KEY (user_id) REFERENCES public.users(user_id)
);
CREATE INDEX idx_device_token_user ON public.device_tokens USING btree (user_id);


-- public.emergency_alerts definition

-- Drop table

-- DROP TABLE public.emergency_alerts;

CREATE TABLE public.emergency_alerts (
	emergency_alert_id uuid NOT NULL,
    emergency_alert_created_at timestamp(6) NOT NULL,
	latitude float8 NOT NULL,
	link_id uuid NOT NULL,
	longitude float8 NOT NULL,
	primary_host_user_id uuid NOT NULL,
	protected_user_id uuid NOT NULL,
	resolution_note varchar(500) NULL,
	resolution_type varchar(30) NULL,
	resolved_at timestamp(6) NULL,
	resolved_by_user_id uuid NULL,
    emergency_alert_status varchar(20) NOT NULL,
	CONSTRAINT emergency_alerts_pkey PRIMARY KEY (emergency_alert_id),
	CONSTRAINT emergency_alerts_resolution_type_check CHECK (((resolution_type)::text = ANY ((ARRAY['FALSE_ALARM'::character varying, 'ALL_SAFE'::character varying, 'POLICE_SENT'::character varying])::text[]))),
	CONSTRAINT emergency_alerts_status_check CHECK (((emergency_alert_status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'RESOLVED'::character varying])::text[]))),
	CONSTRAINT fk2639vjvq7an84urstd9gb8h6l FOREIGN KEY (resolved_by_user_id) REFERENCES public.users(user_id),
	CONSTRAINT fkbsvhcjd8nlql9ttroj2ra44a7 FOREIGN KEY (protected_user_id) REFERENCES public.users(user_id),
	CONSTRAINT fko42p88msah64w0i53tch0bodm FOREIGN KEY (link_id) REFERENCES public.links(link_id),
	CONSTRAINT fkq77lcfnn9yvvd2n0y3mg7up5m FOREIGN KEY (primary_host_user_id) REFERENCES public.users(user_id)
);
CREATE INDEX idx_emergency_host_status ON public.emergency_alerts USING btree (primary_host_user_id, emergency_alert_status);
CREATE INDEX idx_emergency_link_id ON public.emergency_alerts USING btree (link_id);
CREATE INDEX idx_emergency_protected ON public.emergency_alerts USING btree (protected_user_id);


-- public.emergency_audio_recordings definition

-- Drop table

-- DROP TABLE public.emergency_audio_recordings;

CREATE TABLE public.emergency_audio_recordings (
	audio_recording_id uuid NOT NULL,
	audio_recording_created_at timestamp(6) NOT NULL,
	duration_seconds int4 NULL,
	emergency_alert_id uuid NOT NULL,
	file_size_bytes int8 NULL,
	playback_url varchar(2048) NULL,
	audio_recording_status varchar(20) NOT NULL,
	storage_file_id varchar(255) NULL,
	storage_provider varchar(30) NOT NULL,
	uploaded_at timestamp(6) NULL,
	CONSTRAINT emergency_audio_recordings_pkey PRIMARY KEY (audio_recording_id),
	CONSTRAINT emergency_audio_recordings_status_check CHECK (((audio_recording_status)::text = ANY ((ARRAY['RECORDING'::character varying, 'UPLOADED'::character varying, 'FAILED'::character varying])::text[]))),
	CONSTRAINT emergency_audio_recordings_storage_provider_check CHECK (((storage_provider)::text = ANY ((ARRAY['GOOGLE_DRIVE'::character varying, 'LOCAL'::character varying])::text[]))),
	CONSTRAINT fk7662oqjkfke3rqawpavm56cve FOREIGN KEY (emergency_alert_id) REFERENCES public.emergency_alerts(emergency_alert_id)
);
CREATE INDEX idx_emergency_audio_alert ON public.emergency_audio_recordings USING btree (emergency_alert_id, audio_recording_created_at);


-- public.family_groups definition

-- Drop table

-- DROP TABLE public.family_groups;

CREATE TABLE public.family_groups (
	family_group_id uuid NOT NULL,
    family_group_created_at timestamp(6) NOT NULL,
    family_group_name varchar(100) NOT NULL,
	primary_host_user_id uuid NOT NULL,
	CONSTRAINT family_groups_pkey PRIMARY KEY (family_group_id),
	CONSTRAINT fkjjvfhlch7k8w82jcmtyjwlr7o FOREIGN KEY (primary_host_user_id) REFERENCES public.users(user_id)
);
CREATE INDEX idx_family_group_primary_host ON public.family_groups USING btree (primary_host_user_id);


-- public.family_invitations definition

-- Drop table

-- DROP TABLE public.family_invitations;

CREATE TABLE public.family_invitations (
	family_invitation_id uuid NOT NULL,
	accepted_at timestamp(6) NULL,
	accepted_by_user_id uuid NULL,
	family_invitation_created_at timestamp(6) NOT NULL,
	expires_at timestamp(6) NOT NULL,
	family_group_id uuid NOT NULL,
	invited_by_user_id uuid NOT NULL,
	family_invitation_status varchar(20) NOT NULL,
	target_role varchar(30) NOT NULL,
	family_invitation_token varchar(20) NOT NULL,
    CONSTRAINT family_invitations_pkey PRIMARY KEY (family_invitation_id),
    CONSTRAINT family_invitations_status_check CHECK (((family_invitation_status)::text = ANY ((ARRAY['PENDING'::character varying, 'ACCEPTED'::character varying, 'EXPIRED'::character varying, 'CANCELLED'::character varying])::text[]))),
    CONSTRAINT family_invitations_target_role_check CHECK (((target_role)::text = ANY ((ARRAY['PRIMARY_HOST'::character varying, 'SECONDARY_HOST'::character varying, 'PROTECTED'::character varying])::text[]))),
    CONSTRAINT uk14oa9o75ucmuymj5invwal2bo UNIQUE (family_invitation_token),
    CONSTRAINT fkaokm95ewirbumidrloee7bkcr FOREIGN KEY (family_group_id) REFERENCES public.family_groups(family_group_id),
    CONSTRAINT fkcuuyq05jdhyhiowqsdt5e0eu7 FOREIGN KEY (invited_by_user_id) REFERENCES public.users(user_id),
    CONSTRAINT fkiiim2f0jvwjnckmcarpl5dll4 FOREIGN KEY (accepted_by_user_id) REFERENCES public.users(user_id)
);
CREATE INDEX idx_family_invitation_group ON public.family_invitations USING btree (family_group_id);
CREATE INDEX idx_family_invitation_inviter ON public.family_invitations USING btree (invited_by_user_id);


-- public.identity_verifications definition

-- Drop table

-- DROP TABLE public.identity_verifications;

CREATE TABLE public.identity_verifications (
	verification_id uuid NOT NULL,
	challenge_code varchar(20) NOT NULL,
	claimed_person varchar(100) NOT NULL,
	verification_created_at timestamp(6) NOT NULL,
	expires_at timestamp(6) NOT NULL,
	host_user_id uuid NOT NULL,
	link_id uuid NOT NULL,
	protected_user_id uuid NOT NULL,
	resolution_note varchar(500) NULL,
	resolved_at timestamp(6) NULL,
	verification_status varchar(20) NOT NULL,
    CONSTRAINT identity_verifications_pkey PRIMARY KEY (verification_id),
    CONSTRAINT identity_verifications_status_check CHECK (((verification_status)::text = ANY ((ARRAY['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying, 'EXPIRED'::character varying])::text[]))),
    CONSTRAINT fk6ra22328ytl217gekebm5p4bm FOREIGN KEY (host_user_id) REFERENCES public.users(user_id),
    CONSTRAINT fk7hb8kou364k2rf26xcb4uidv3 FOREIGN KEY (link_id) REFERENCES public.links(link_id),
    CONSTRAINT fkfhdyn665islicercydrm5uh6t FOREIGN KEY (protected_user_id) REFERENCES public.users(user_id)
);
CREATE INDEX idx_verification_host_id ON public.identity_verifications USING btree (host_user_id);
CREATE INDEX idx_verification_link_id ON public.identity_verifications USING btree (link_id);
CREATE INDEX idx_verification_protected_id ON public.identity_verifications USING btree (protected_user_id);
CREATE INDEX idx_verification_status ON public.identity_verifications USING btree (verification_status);

-- public.invitations definition

-- Drop table

-- DROP TABLE public.invitations;

CREATE TABLE public.invitations (
	invitation_id uuid NOT NULL,
	accepted_at timestamp(6) NULL,
	accepted_by_user_id uuid NULL,
	invitation_created_at timestamp(6) NOT NULL,
	expires_at timestamp(6) NOT NULL,
	host_id uuid NOT NULL,
	host_name varchar(100) NULL,
	invitation_status varchar(20) NOT NULL,
	invitation_token varchar(20) NOT NULL,
    CONSTRAINT invitations_pkey PRIMARY KEY (invitation_id),
    CONSTRAINT invitations_status_check CHECK (((invitation_status)::text = ANY ((ARRAY['PENDING'::character varying, 'ACCEPTED'::character varying, 'EXPIRED'::character varying, 'CANCELLED'::character varying])::text[]))),
    CONSTRAINT ukt4i6esv44p6yi7cxq277vlo3i UNIQUE (invitation_token),
    CONSTRAINT fkbk4a8bfo0266d0pcqhprqixk8 FOREIGN KEY (host_id) REFERENCES public.users(user_id),
    CONSTRAINT fkhbffs1oebaxvyl7an4ybwsbsj FOREIGN KEY (accepted_by_user_id) REFERENCES public.users(user_id)
);

-- public.family_group_members definition

-- Drop table

-- DROP TABLE public.family_group_members;

CREATE TABLE public.family_group_members (
	family_group_member_id uuid NOT NULL,
	family_group_id uuid NOT NULL,
	joined_at timestamp(6) NOT NULL,
	family_group_memberm_role varchar(30) NOT NULL,
	user_id uuid NOT NULL,
    CONSTRAINT family_group_members_pkey PRIMARY KEY (family_group_member_id),
    CONSTRAINT family_group_members_role_check CHECK (((family_group_member_role)::text = ANY ((ARRAY['PRIMARY_HOST'::character varying, 'SECONDARY_HOST'::character varying, 'PROTECTED'::character varying])::text[]))),
    CONSTRAINT uk_family_group_user UNIQUE (family_group_id, user_id),
    CONSTRAINT fkpwvficnxvxx1d3rly40u79tj FOREIGN KEY (user_id) REFERENCES public.users(user_id),
    CONSTRAINT fkrkkcj57g69olxue8elbjr7i10 FOREIGN KEY (family_group_id) REFERENCES public.family_groups(family_group_id)
);
CREATE INDEX idx_family_member_group ON public.family_group_members USING btree (family_group_id);
CREATE INDEX idx_family_member_user ON public.family_group_members USING btree (user_id);