package me.kyleclemens.ffxivloglib.entry

enum class FFXIVEntryType(val code: Int) {

    SYSTEM_MESSAGE(0x03),
    SAY(0x0a),
    SHOUT(0x0b),
    REPLY(0x0c),
    TELL(0x0d),
    PARTY(0x0e),
    LINKSHELL_1(0x10),
    LINKSHELL_2(0x11),
    LINKSHELL_3(0x12),
    LINKSHELL_4(0x13),
    LINKSHELL_5(0x14),
    LINKSHELL_6(0x15),
    LINKSHELL_7(0x16),
    LINKSHELL_8(0x17),
    FREE_COMPANY_CHAT(0x18),
    CUSTOM_EMOTE(0x1c),
    EMOTE(0x1d),
    YELL(0x1e),
    ITEM_GAINED(0x3e),
    ECHO(0x38),
    CLIENT_MESSAGE(0x39),
    DUTY_FINDER_UPDATE(0x3c),
    REWARD_RECEIVED(0x3e),
    EXPERIENCE_GAINED(0x40),
    LOOT(0x41),
    NPC_CHAT(0x44),
    FREE_COMPANY_EVENT(0x45),
    LOG_IN_OUT(0x46),
    MARKET_BOARD(0x47),
    PARTY_FINDER_UPDATE(0x48),
    PARTY_MARK(0x49),
    RANDOM(0x4a),
    TRIAL_UPDATE(0xb9),
    UNKNOWN(0xFF);

    companion object {
        fun fromCode(code: Int) = FFXIVEntryType.values().find { it.code == code } ?: FFXIVEntryType.UNKNOWN
    }

}
