INSERT INTO complaint_responses
(id, complaint_id, generated_response, status, generated_at, confidence_score, tone, auto_approved)
VALUES
    (
        100,
        100,
        'This is a sample generated response pending approval.',
        'PENDING_APPROVAL',
        '2026-01-18 5:00:00',
        0.91,
        'Empathetic',
     false
    );
