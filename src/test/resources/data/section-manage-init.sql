INSERT INTO station (id, name) VALUES(4, '청계산입구역'), (2, '판교역'), (6, '선릉역'), (7, '남성역'), (8, '숭실대입구역'), (9, '대방역'), (10, '이수역');
INSERT INTO line (id, name, color, up_station_id, down_station_id, distance) VALUES (1, '신분당선', 'red', 4, 2, 10);
INSERT INTO section (id, up_station_id, down_station_id, distance, line_id) VALUES (2, 4, 2, 10, 1);