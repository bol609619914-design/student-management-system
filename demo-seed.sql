UPDATE students SET student_no='20260002', name='张三', classroom='软件工程1班', major='软件工程', enrollment_date='2026-09-01', current_status='在读', photo_url='https://images.unsplash.com/photo-1500648767791-00dcc994a43e', gender='男', phone='13800000001' WHERE id=1;
UPDATE students SET student_no='20260003', name='李思雨', classroom='计算机科学与技术2班', major='计算机科学与技术', enrollment_date='2026-09-01', current_status='在读', photo_url='https://images.unsplash.com/photo-1494790108377-be9c29b29330', gender='女', phone='13800000002' WHERE id=2;
UPDATE students SET student_no='20260004', name='王志强', classroom='网络工程1班', major='网络工程', enrollment_date='2025-09-01', current_status='在读', photo_url='https://images.unsplash.com/photo-1506794778202-cad84cf45f1d', gender='男', phone='13800000003' WHERE id=3;
UPDATE students SET current_status='在读', photo_url='https://images.unsplash.com/photo-1568602471122-7832951cc4c5', major='计算机科学与技术', phone='13800003000' WHERE id=4;

INSERT INTO app_users (username,password,full_name,role,phone,enabled,avatar_url)
SELECT 'teacher2','$2a$10$xQvM6Fj6X2y5p0jKfT1xF.6x0j7mL7F7vXG6QeY2HnE0xD2Vf0Q7i','李老师','TEACHER','13800002001',b'1','https://images.unsplash.com/photo-1544005313-94ddf0286df2'
WHERE NOT EXISTS (SELECT 1 FROM app_users WHERE username='teacher2');
INSERT INTO app_users (username,password,full_name,role,phone,enabled,avatar_url)
SELECT 'teacher3','$2a$10$xQvM6Fj6X2y5p0jKfT1xF.6x0j7mL7F7vXG6QeY2HnE0xD2Vf0Q7i','陈老师','TEACHER','13800002002',b'1','https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91'
WHERE NOT EXISTS (SELECT 1 FROM app_users WHERE username='teacher3');
INSERT INTO app_users (username,password,full_name,role,phone,enabled,avatar_url)
SELECT 'student2','$2a$10$xQvM6Fj6X2y5p0jKfT1xF.6x0j7mL7F7vXG6QeY2HnE0xD2Vf0Q7i','赵欣怡','STUDENT','13800003001',b'1','https://images.unsplash.com/photo-1488426862026-3ee34a7d66df'
WHERE NOT EXISTS (SELECT 1 FROM app_users WHERE username='student2');
INSERT INTO app_users (username,password,full_name,role,phone,enabled,avatar_url)
SELECT 'student3','$2a$10$xQvM6Fj6X2y5p0jKfT1xF.6x0j7mL7F7vXG6QeY2HnE0xD2Vf0Q7i','孙浩然','STUDENT','13800003002',b'1','https://images.unsplash.com/photo-1504593811423-6dd665756598'
WHERE NOT EXISTS (SELECT 1 FROM app_users WHERE username='student3');
INSERT INTO app_users (username,password,full_name,role,phone,enabled,avatar_url)
SELECT 'student4','$2a$10$xQvM6Fj6X2y5p0jKfT1xF.6x0j7mL7F7vXG6QeY2HnE0xD2Vf0Q7i','周雨桐','STUDENT','13800003003',b'1','https://images.unsplash.com/photo-1438761681033-6461ffad8d80'
WHERE NOT EXISTS (SELECT 1 FROM app_users WHERE username='student4');

INSERT INTO students (student_no,name,age,gender,classroom,phone,major,enrollment_date,photo_url,current_status,user_id)
SELECT '20260005','赵欣怡',19,'女','软件工程2班','13800003001','软件工程','2026-09-01','https://images.unsplash.com/photo-1488426862026-3ee34a7d66df','在读',u.id FROM app_users u
WHERE u.username='student2' AND NOT EXISTS (SELECT 1 FROM students s WHERE s.user_id=u.id);
INSERT INTO students (student_no,name,age,gender,classroom,phone,major,enrollment_date,photo_url,current_status,user_id)
SELECT '20260006','孙浩然',20,'男','计算机科学与技术1班','13800003002','计算机科学与技术','2025-09-01','https://images.unsplash.com/photo-1504593811423-6dd665756598','在读',u.id FROM app_users u
WHERE u.username='student3' AND NOT EXISTS (SELECT 1 FROM students s WHERE s.user_id=u.id);
INSERT INTO students (student_no,name,age,gender,classroom,phone,major,enrollment_date,photo_url,current_status,user_id)
SELECT '20260007','周雨桐',18,'女','数据科学1班','13800003003','数据科学与大数据技术','2026-09-01','https://images.unsplash.com/photo-1438761681033-6461ffad8d80','在读',u.id FROM app_users u
WHERE u.username='student4' AND NOT EXISTS (SELECT 1 FROM students s WHERE s.user_id=u.id);

INSERT INTO courses (course_code,course_name,credit,class_hours,nature,teacher_id,classroom,week_day,start_section,end_section,capacity,description)
SELECT 'CS303','操作系统',4,64,'REQUIRED',u.id,'C201','周二',1,2,60,'操作系统原理与实验课程' FROM app_users u
WHERE u.username='teacher2' AND NOT EXISTS (SELECT 1 FROM courses WHERE course_code='CS303');
INSERT INTO courses (course_code,course_name,credit,class_hours,nature,teacher_id,classroom,week_day,start_section,end_section,capacity,description)
SELECT 'CS304','计算机网络',3,48,'REQUIRED',u.id,'C302','周四',3,4,60,'网络体系结构、协议与实践' FROM app_users u
WHERE u.username='teacher2' AND NOT EXISTS (SELECT 1 FROM courses WHERE course_code='CS304');
INSERT INTO courses (course_code,course_name,credit,class_hours,nature,teacher_id,classroom,week_day,start_section,end_section,capacity,description)
SELECT 'CS305','数据结构',4,64,'REQUIRED',u.id,'A205','周五',1,2,55,'线性结构、树、图与算法分析' FROM app_users u
WHERE u.username='teacher3' AND NOT EXISTS (SELECT 1 FROM courses WHERE course_code='CS305');
INSERT INTO courses (course_code,course_name,credit,class_hours,nature,teacher_id,classroom,week_day,start_section,end_section,capacity,description)
SELECT 'CS306','Python程序设计',2,32,'ELECTIVE',u.id,'B105','周三',5,6,45,'适合跨学科学生选修的编程入门课' FROM app_users u
WHERE u.username='teacher3' AND NOT EXISTS (SELECT 1 FROM courses WHERE course_code='CS306');

INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-01 09:00:00' FROM students s JOIN courses c ON c.course_code='CS301'
WHERE s.student_no='20260001' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-01 09:02:00' FROM students s JOIN courses c ON c.course_code='CS302'
WHERE s.student_no='20260001' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-01 09:05:00' FROM students s JOIN courses c ON c.course_code='CS303'
WHERE s.student_no='20260001' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-02 10:00:00' FROM students s JOIN courses c ON c.course_code='CS301'
WHERE s.student_no='20260003' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-02 10:02:00' FROM students s JOIN courses c ON c.course_code='CS304'
WHERE s.student_no='20260003' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-02 10:05:00' FROM students s JOIN courses c ON c.course_code='CS306'
WHERE s.student_no='20260003' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-03 11:00:00' FROM students s JOIN courses c ON c.course_code='CS302'
WHERE s.student_no='20260004' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-03 11:04:00' FROM students s JOIN courses c ON c.course_code='CS303'
WHERE s.student_no='20260004' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-03 11:08:00' FROM students s JOIN courses c ON c.course_code='CS305'
WHERE s.student_no='20260004' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-04 09:00:00' FROM students s JOIN courses c ON c.course_code='CS301'
WHERE s.student_no='20260005' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-04 09:02:00' FROM students s JOIN courses c ON c.course_code='CS305'
WHERE s.student_no='20260005' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-04 09:06:00' FROM students s JOIN courses c ON c.course_code='CS306'
WHERE s.student_no='20260005' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-05 08:30:00' FROM students s JOIN courses c ON c.course_code='CS302'
WHERE s.student_no='20260006' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-05 08:35:00' FROM students s JOIN courses c ON c.course_code='CS304'
WHERE s.student_no='20260006' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-05 08:40:00' FROM students s JOIN courses c ON c.course_code='CS305'
WHERE s.student_no='20260006' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-06 14:00:00' FROM students s JOIN courses c ON c.course_code='CS301'
WHERE s.student_no='20260007' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-06 14:03:00' FROM students s JOIN courses c ON c.course_code='CS304'
WHERE s.student_no='20260007' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);
INSERT INTO enrollments (student_id,course_id,selected_at)
SELECT s.id,c.id,'2026-03-06 14:07:00' FROM students s JOIN courses c ON c.course_code='CS306'
WHERE s.student_no='20260007' AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.student_id=s.id AND e.course_id=c.id);

INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,88,84,91,88.4,3.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260001' AND c.course_code='CS301' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,79,82,85,82.3,3.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260001' AND c.course_code='CS302' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,91,89,93,91.1,4.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260001' AND c.course_code='CS303' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,87,80,78,81.1,3.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260003' AND c.course_code='CS301' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,92,88,94,91.6,4.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260003' AND c.course_code='CS304' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,76,73,81,77.3,2.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260003' AND c.course_code='CS306' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,65,68,72,68.9,1.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260004' AND c.course_code='CS302' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,58,60,55,57.4,0.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260004' AND c.course_code='CS303' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,82,78,80,80.0,3.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260004' AND c.course_code='CS305' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,95,90,92,92.3,4.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260005' AND c.course_code='CS301' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,89,87,86,87.2,3.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260005' AND c.course_code='CS305' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,78,75,79,77.5,2.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260005' AND c.course_code='CS306' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,84,82,85,83.8,3.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260006' AND c.course_code='CS302' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,88,90,87,88.3,3.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260006' AND c.course_code='CS304' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,93,91,94,92.8,4.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260006' AND c.course_code='CS305' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,90,86,88,88.0,3.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260007' AND c.course_code='CS301' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,72,69,74,71.8,2.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260007' AND c.course_code='CS304' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);
INSERT INTO grade_records (enrollment_id,daily_score,midterm_score,final_score,total_score,gpa)
SELECT e.id,85,88,90,87.9,3.0 FROM enrollments e JOIN students s ON s.id=e.student_id JOIN courses c ON c.id=e.course_id WHERE s.student_no='20260007' AND c.course_code='CS306' AND NOT EXISTS (SELECT 1 FROM grade_records g WHERE g.enrollment_id=e.id);

INSERT INTO announcements (title,content,pinned,published_at,publisher_id)
SELECT '五一放假安排','请各班同学于放假前完成宿舍安全检查，并按时返校。',b'1','2026-04-01 09:00:00',1 WHERE NOT EXISTS (SELECT 1 FROM announcements WHERE title='五一放假安排');
INSERT INTO announcements (title,content,pinned,published_at,publisher_id)
SELECT '2026届毕业设计答辩通知','答辩时间暂定于6月上旬，请同学们提前准备论文与演示材料。',b'0','2026-04-03 14:30:00',1 WHERE NOT EXISTS (SELECT 1 FROM announcements WHERE title='2026届毕业设计答辩通知');
INSERT INTO announcements (title,content,pinned,published_at,publisher_id)
SELECT '数据库系统实验课调课通知','本周数据库系统实验课调整至周五下午第5-6节，地点不变。',b'0','2026-04-04 10:20:00',2 WHERE NOT EXISTS (SELECT 1 FROM announcements WHERE title='数据库系统实验课调课通知');

INSERT INTO leave_requests (student_id,reason,start_date,end_date,status,approval_comment,attendance_linked,submitted_at,approver_id)
SELECT s.id,'发烧就诊','2026-04-07','2026-04-08','APPROVED','已批准，记得补交病假证明',b'1','2026-04-06 08:30:00',2 FROM students s WHERE s.student_no='20260001' AND NOT EXISTS (SELECT 1 FROM leave_requests WHERE student_id=s.id AND reason='发烧就诊');
INSERT INTO leave_requests (student_id,reason,start_date,end_date,status,approval_comment,attendance_linked,submitted_at,approver_id)
SELECT s.id,'参加省赛培训','2026-04-10','2026-04-12','PENDING',NULL,b'0','2026-04-06 09:10:00',NULL FROM students s WHERE s.student_no='20260005' AND NOT EXISTS (SELECT 1 FROM leave_requests WHERE student_id=s.id AND reason='参加省赛培训');
INSERT INTO leave_requests (student_id,reason,start_date,end_date,status,approval_comment,attendance_linked,submitted_at,approver_id)
SELECT s.id,'家庭事务请假','2026-04-09','2026-04-09','REJECTED','课程周较紧，请与辅导员沟通后重新提交',b'0','2026-04-05 16:00:00',1 FROM students s WHERE s.student_no='20260007' AND NOT EXISTS (SELECT 1 FROM leave_requests WHERE student_id=s.id AND reason='家庭事务请假');

INSERT INTO student_status_changes (student_id,change_type,before_status,after_status,reason,change_date)
SELECT s.id,'ENROLL','新生','在读','完成报到注册','2026-09-01' FROM students s WHERE s.student_no='20260005' AND NOT EXISTS (SELECT 1 FROM student_status_changes WHERE student_id=s.id AND change_type='ENROLL');
INSERT INTO student_status_changes (student_id,change_type,before_status,after_status,reason,change_date)
SELECT s.id,'CHANGE_MAJOR','网络工程','计算机科学与技术','根据个人发展方向申请转专业','2026-03-15' FROM students s WHERE s.student_no='20260004' AND NOT EXISTS (SELECT 1 FROM student_status_changes WHERE student_id=s.id AND change_type='CHANGE_MAJOR');
INSERT INTO student_status_changes (student_id,change_type,before_status,after_status,reason,change_date)
SELECT s.id,'SUSPEND','在读','休学','因身体原因申请休学一学期','2025-12-20' FROM students s WHERE s.student_no='20260006' AND NOT EXISTS (SELECT 1 FROM student_status_changes WHERE student_id=s.id AND change_type='SUSPEND');
INSERT INTO student_status_changes (student_id,change_type,before_status,after_status,reason,change_date)
SELECT s.id,'RESUME','休学','在读','审核通过后恢复学籍','2026-03-01' FROM students s WHERE s.student_no='20260006' AND NOT EXISTS (SELECT 1 FROM student_status_changes WHERE student_id=s.id AND change_type='RESUME');
