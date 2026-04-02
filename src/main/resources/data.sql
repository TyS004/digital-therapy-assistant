INSERT INTO session_module (id) 
SELECT 'mod-1' WHERE NOT EXISTS (SELECT 1 FROM session_module WHERE id = 'mod-1');

INSERT INTO session_module (id) 
SELECT 'mod-2' WHERE NOT EXISTS (SELECT 1 FROM session_module WHERE id = 'mod-2');

INSERT INTO cbt_session (id, title, description, duration_minutes, order_index, session_module_id) 
SELECT '550e8400-e29b-41d4-a716-446655440000', 'Introduction to CBT', 'Learn the basics of CBT', 45, 1, 'mod-1'
WHERE NOT EXISTS (SELECT 1 FROM cbt_session WHERE id = '550e8400-e29b-41d4-a716-446655440000');

INSERT INTO cbt_session (id, title, description, duration_minutes, order_index, session_module_id) 
SELECT '550e8400-e29b-41d4-a716-446655440001', 'Understanding Burnout', 'Identify signs of burnout', 60, 2, 'mod-1'
WHERE NOT EXISTS (SELECT 1 FROM cbt_session WHERE id = '550e8400-e29b-41d4-a716-446655440001');

INSERT INTO cbt_session (id, title, description, duration_minutes, order_index, session_module_id) 
SELECT '550e8400-e29b-41d4-a716-446655440002', 'Thought Challenging', 'Challenge cognitive distortions', 50, 1, 'mod-2'
WHERE NOT EXISTS (SELECT 1 FROM cbt_session WHERE id = '550e8400-e29b-41d4-a716-446655440002');

INSERT INTO cbt_session (id, title, description, duration_minutes, order_index, session_module_id) 
SELECT '550e8400-e29b-41d4-a716-446655440003', 'Behavioral Activation', 'Re-engage with meaningful activities', 45, 1, 'mod-2'
WHERE NOT EXISTS (SELECT 1 FROM cbt_session WHERE id = '550e8400-e29b-41d4-a716-446655440003');

INSERT INTO cognitive_distortion (id, name, description) 
SELECT 'all-or-nothing', 'All-or-Nothing Thinking', 'Seeing things in black-and-white categories'
WHERE NOT EXISTS (SELECT 1 FROM cognitive_distortion WHERE id = 'all-or-nothing');

INSERT INTO cognitive_distortion (id, name, description) 
SELECT 'catastrophizing', 'Catastrophizing', 'Expecting the worst possible outcome'
WHERE NOT EXISTS (SELECT 1 FROM cognitive_distortion WHERE id = 'catastrophizing');

INSERT INTO cognitive_distortion (id, name, description) 
SELECT 'mind-reading', 'Mind Reading', 'Assuming you know what others are thinking'
WHERE NOT EXISTS (SELECT 1 FROM cognitive_distortion WHERE id = 'mind-reading');

INSERT INTO cognitive_distortion (id, name, description) 
SELECT 'overgeneralization', 'Overgeneralization', 'Making broad conclusions from a single event'
WHERE NOT EXISTS (SELECT 1 FROM cognitive_distortion WHERE id = 'overgeneralization');

INSERT INTO cognitive_distortion (id, name, description) 
SELECT 'should-statements', 'Should Statements', 'Using rigid rules about how you or others should behave'
WHERE NOT EXISTS (SELECT 1 FROM cognitive_distortion WHERE id = 'should-statements');