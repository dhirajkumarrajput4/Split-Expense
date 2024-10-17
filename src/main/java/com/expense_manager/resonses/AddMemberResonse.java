package com.expense_manager.resonses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMemberResonse {
    private String emailId;
    private Long groupId;
}
