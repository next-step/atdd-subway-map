INSERT INTO station (id, name) VALUES(4, '청계산입구역'), (2, '판교역'), (6, '정자역'), (11, '미금역');
INSERT INTO line (id, name, color, up_station_id, down_station_id, distance) VALUES (1, '신분당선', 'red', 4, 2, 10);
INSERT INTO section (id, up_station_id, down_station_id, distance, line_id) VALUES (5, 4, 2, 10, 1);