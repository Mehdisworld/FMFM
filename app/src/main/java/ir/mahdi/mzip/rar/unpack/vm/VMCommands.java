package ir.mahdi.mzip.rar.unpack.vm;

public enum VMCommands {
    VM_MOV(0),
    VM_CMP(1),
    VM_ADD(2),
    VM_SUB(3),
    VM_JZ(4),
    VM_JNZ(5),
    VM_INC(6),
    VM_DEC(7),
    VM_JMP(8),
    VM_XOR(9),
    VM_AND(10),
    VM_OR(11),
    VM_TEST(12),
    VM_JS(13),
    VM_JNS(14),
    VM_JB(15),
    VM_JBE(16),
    VM_JA(17),
    VM_JAE(18),
    VM_PUSH(19),
    VM_POP(20),
    VM_CALL(21),
    VM_RET(22),
    VM_NOT(23),
    VM_SHL(24),
    VM_SHR(25),
    VM_SAR(26),
    VM_NEG(27),
    VM_PUSHA(28),
    VM_POPA(29),
    VM_PUSHF(30),
    VM_POPF(31),
    VM_MOVZX(32),
    VM_MOVSX(33),
    VM_XCHG(34),
    VM_MUL(35),
    VM_DIV(36),
    VM_ADC(37),
    VM_SBB(38),
    VM_PRINT(39),
    VM_MOVB(40),
    VM_MOVD(41),
    VM_CMPB(42),
    VM_CMPD(43),
    VM_ADDB(44),
    VM_ADDD(45),
    VM_SUBB(46),
    VM_SUBD(47),
    VM_INCB(48),
    VM_INCD(49),
    VM_DECB(50),
    VM_DECD(51),
    VM_NEGB(52),
    VM_NEGD(53),
    VM_STANDARD(54);
    
    private int vmCommand;

    private VMCommands(int i) {
        this.vmCommand = i;
    }

    public static VMCommands findVMCommand(int i) {
        VMCommands vMCommands = VM_MOV;
        if (vMCommands.equals(i)) {
            return vMCommands;
        }
        VMCommands vMCommands2 = VM_CMP;
        if (vMCommands2.equals(i)) {
            return vMCommands2;
        }
        VMCommands vMCommands3 = VM_ADD;
        if (vMCommands3.equals(i)) {
            return vMCommands3;
        }
        VMCommands vMCommands4 = VM_SUB;
        if (vMCommands4.equals(i)) {
            return vMCommands4;
        }
        VMCommands vMCommands5 = VM_JZ;
        if (vMCommands5.equals(i)) {
            return vMCommands5;
        }
        VMCommands vMCommands6 = VM_JNZ;
        if (vMCommands6.equals(i)) {
            return vMCommands6;
        }
        VMCommands vMCommands7 = VM_INC;
        if (vMCommands7.equals(i)) {
            return vMCommands7;
        }
        VMCommands vMCommands8 = VM_DEC;
        if (vMCommands8.equals(i)) {
            return vMCommands8;
        }
        VMCommands vMCommands9 = VM_JMP;
        if (vMCommands9.equals(i)) {
            return vMCommands9;
        }
        VMCommands vMCommands10 = VM_XOR;
        if (vMCommands10.equals(i)) {
            return vMCommands10;
        }
        VMCommands vMCommands11 = VM_AND;
        if (vMCommands11.equals(i)) {
            return vMCommands11;
        }
        VMCommands vMCommands12 = VM_OR;
        if (vMCommands12.equals(i)) {
            return vMCommands12;
        }
        VMCommands vMCommands13 = VM_TEST;
        if (vMCommands13.equals(i)) {
            return vMCommands13;
        }
        VMCommands vMCommands14 = VM_JS;
        if (vMCommands14.equals(i)) {
            return vMCommands14;
        }
        VMCommands vMCommands15 = VM_JNS;
        if (vMCommands15.equals(i)) {
            return vMCommands15;
        }
        VMCommands vMCommands16 = VM_JB;
        if (vMCommands16.equals(i)) {
            return vMCommands16;
        }
        VMCommands vMCommands17 = VM_JBE;
        if (vMCommands17.equals(i)) {
            return vMCommands17;
        }
        VMCommands vMCommands18 = VM_JA;
        if (vMCommands18.equals(i)) {
            return vMCommands18;
        }
        VMCommands vMCommands19 = VM_JAE;
        if (vMCommands19.equals(i)) {
            return vMCommands19;
        }
        VMCommands vMCommands20 = VM_PUSH;
        if (vMCommands20.equals(i)) {
            return vMCommands20;
        }
        VMCommands vMCommands21 = VM_POP;
        if (vMCommands21.equals(i)) {
            return vMCommands21;
        }
        VMCommands vMCommands22 = VM_CALL;
        if (vMCommands22.equals(i)) {
            return vMCommands22;
        }
        VMCommands vMCommands23 = VM_RET;
        if (vMCommands23.equals(i)) {
            return vMCommands23;
        }
        VMCommands vMCommands24 = VM_NOT;
        if (vMCommands24.equals(i)) {
            return vMCommands24;
        }
        VMCommands vMCommands25 = VM_SHL;
        if (vMCommands25.equals(i)) {
            return vMCommands25;
        }
        VMCommands vMCommands26 = VM_SHR;
        if (vMCommands26.equals(i)) {
            return vMCommands26;
        }
        VMCommands vMCommands27 = VM_SAR;
        if (vMCommands27.equals(i)) {
            return vMCommands27;
        }
        VMCommands vMCommands28 = VM_NEG;
        if (vMCommands28.equals(i)) {
            return vMCommands28;
        }
        VMCommands vMCommands29 = VM_PUSHA;
        if (vMCommands29.equals(i)) {
            return vMCommands29;
        }
        VMCommands vMCommands30 = VM_POPA;
        if (vMCommands30.equals(i)) {
            return vMCommands30;
        }
        VMCommands vMCommands31 = VM_PUSHF;
        if (vMCommands31.equals(i)) {
            return vMCommands31;
        }
        VMCommands vMCommands32 = VM_POPF;
        if (vMCommands32.equals(i)) {
            return vMCommands32;
        }
        VMCommands vMCommands33 = VM_MOVZX;
        if (vMCommands33.equals(i)) {
            return vMCommands33;
        }
        VMCommands vMCommands34 = VM_MOVSX;
        if (vMCommands34.equals(i)) {
            return vMCommands34;
        }
        VMCommands vMCommands35 = VM_XCHG;
        if (vMCommands35.equals(i)) {
            return vMCommands35;
        }
        VMCommands vMCommands36 = VM_MUL;
        if (vMCommands36.equals(i)) {
            return vMCommands36;
        }
        VMCommands vMCommands37 = VM_DIV;
        if (vMCommands37.equals(i)) {
            return vMCommands37;
        }
        VMCommands vMCommands38 = VM_ADC;
        if (vMCommands38.equals(i)) {
            return vMCommands38;
        }
        VMCommands vMCommands39 = VM_SBB;
        if (vMCommands39.equals(i)) {
            return vMCommands39;
        }
        VMCommands vMCommands40 = VM_PRINT;
        if (vMCommands40.equals(i)) {
            return vMCommands40;
        }
        VMCommands vMCommands41 = VM_MOVB;
        if (vMCommands41.equals(i)) {
            return vMCommands41;
        }
        VMCommands vMCommands42 = VM_MOVD;
        if (vMCommands42.equals(i)) {
            return vMCommands42;
        }
        VMCommands vMCommands43 = VM_CMPB;
        if (vMCommands43.equals(i)) {
            return vMCommands43;
        }
        VMCommands vMCommands44 = VM_CMPD;
        if (vMCommands44.equals(i)) {
            return vMCommands44;
        }
        VMCommands vMCommands45 = VM_ADDB;
        if (vMCommands45.equals(i)) {
            return vMCommands45;
        }
        VMCommands vMCommands46 = VM_ADDD;
        if (vMCommands46.equals(i)) {
            return vMCommands46;
        }
        VMCommands vMCommands47 = VM_SUBB;
        if (vMCommands47.equals(i)) {
            return vMCommands47;
        }
        VMCommands vMCommands48 = VM_SUBD;
        if (vMCommands48.equals(i)) {
            return vMCommands48;
        }
        VMCommands vMCommands49 = VM_INCB;
        if (vMCommands49.equals(i)) {
            return vMCommands49;
        }
        VMCommands vMCommands50 = VM_INCD;
        if (vMCommands50.equals(i)) {
            return vMCommands50;
        }
        VMCommands vMCommands51 = VM_DECB;
        if (vMCommands51.equals(i)) {
            return vMCommands51;
        }
        VMCommands vMCommands52 = VM_DECD;
        if (vMCommands52.equals(i)) {
            return vMCommands52;
        }
        VMCommands vMCommands53 = VM_NEGB;
        if (vMCommands53.equals(i)) {
            return vMCommands53;
        }
        VMCommands vMCommands54 = VM_NEGD;
        if (vMCommands54.equals(i)) {
            return vMCommands54;
        }
        VMCommands vMCommands55 = VM_STANDARD;
        if (vMCommands55.equals(i)) {
            return vMCommands55;
        }
        return null;
    }

    public int getVMCommand() {
        return this.vmCommand;
    }

    public boolean equals(int i) {
        return this.vmCommand == i;
    }
}
