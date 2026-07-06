package org.yuemi.mmoitems.plugin.item;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

@JsonDeserialize(using = ItemSkillConfig.Deserializer.class)
public record ItemSkillConfig(String skill, String trigger) {

    public static class Deserializer extends StdDeserializer<ItemSkillConfig> {
        public Deserializer() {
            super(ItemSkillConfig.class);
        }

        @Override
        public ItemSkillConfig deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            if (node.isTextual()) {
                return new ItemSkillConfig(node.asText(), null);
            } else if (node.isObject()) {
                String skillName = node.has("skill") ? node.get("skill").asText() : "";
                String triggerName = node.has("trigger") ? node.get("trigger").asText() : null;
                return new ItemSkillConfig(skillName, triggerName);
            }
            return new ItemSkillConfig("", null);
        }
    }
}
