package com.czh.leave.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // 0. Ensure Notification Table Exists
        try {
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS notifications (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id BIGINT NOT NULL, " +
                    "content VARCHAR(255) NOT NULL, " +
                    "is_read TINYINT DEFAULT 0, " +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
        } catch (Exception e) {
            System.out.println("Notification table creation failed or already exists: " + e.getMessage());
        }

        // Initialize Leave Types
        Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM leave_types", Integer.class);
        if (count == 0) {
            jdbcTemplate.execute("INSERT INTO leave_types (name, code, require_attachment, description) VALUES ('浜嬪亣', 'PERSONAL', 0, '鍥犵浜嬭鍋?)");
            jdbcTemplate.execute("INSERT INTO leave_types (name, code, require_attachment, description) VALUES ('鐥呭亣', 'SICK', 1, '鍥犵梾璇峰亣锛岄渶闄勪欢')");
            System.out.println("Initialized Leave Types: Personal & Sick");
        }

        // Initialize Departments
        Integer deptCount = jdbcTemplate.queryForObject("SELECT count(*) FROM departments", Integer.class);
        if (deptCount == 0) {
            jdbcTemplate.execute("INSERT INTO departments (name, code) VALUES ('璁＄畻鏈虹郴', 'CS')");
            System.out.println("Initialized Department: CS");
        }

        // Initialize Approval Flow (Force check and insert)
        // 1. Get IDs
        Long deptId = jdbcTemplate.queryForObject("SELECT id FROM departments WHERE code='CS'", Long.class);
        Long typePersonal = jdbcTemplate.queryForObject("SELECT id FROM leave_types WHERE code='PERSONAL'", Long.class);
        Long typeSick = jdbcTemplate.queryForObject("SELECT id FROM leave_types WHERE code='SICK'", Long.class);

        // 2. Insert Flows if not exist
        Integer personalFlowCount = jdbcTemplate.queryForObject("SELECT count(*) FROM approval_flows WHERE leave_type_id = ?", Integer.class, typePersonal);
        if (personalFlowCount == 0) {
             jdbcTemplate.update("INSERT INTO approval_flows (name, department_id, leave_type_id) VALUES (?, ?, ?)", "璁＄畻鏈虹郴浜嬪亣娴佺▼", deptId, typePersonal);
             System.out.println("Inserted Personal Flow");
        }
        
        Integer sickFlowCount = jdbcTemplate.queryForObject("SELECT count(*) FROM approval_flows WHERE leave_type_id = ?", Integer.class, typeSick);
        if (sickFlowCount == 0) {
             jdbcTemplate.update("INSERT INTO approval_flows (name, department_id, leave_type_id) VALUES (?, ?, ?)", "璁＄畻鏈虹郴鐥呭亣娴佺▼", deptId, typeSick);
             System.out.println("Inserted Sick Flow");
        }
             
        // 3. Insert Steps (Teacher -> Head Teacher)
        Long flowPersonal = jdbcTemplate.queryForObject("SELECT id FROM approval_flows WHERE leave_type_id=?", Long.class, typePersonal);
        Integer pStepCount = jdbcTemplate.queryForObject("SELECT count(*) FROM approval_steps WHERE flow_id = ?", Integer.class, flowPersonal);
        if (pStepCount == 0) {
            jdbcTemplate.update("INSERT INTO approval_steps (flow_id, step_order, approver_role, description) VALUES (?, 1, 'TEACHER', '杈呭鍛樺鎵?)", flowPersonal);
        }
             
        Long flowSick = jdbcTemplate.queryForObject("SELECT id FROM approval_flows WHERE leave_type_id=?", Long.class, typeSick);
        Integer sStepCount = jdbcTemplate.queryForObject("SELECT count(*) FROM approval_steps WHERE flow_id = ?", Integer.class, flowSick);
        if (sStepCount == 0) {
            jdbcTemplate.update("INSERT INTO approval_steps (flow_id, step_order, approver_role, description) VALUES (?, 1, 'TEACHER', '杈呭鍛樺鎵?)", flowSick);
        }
    }
}
