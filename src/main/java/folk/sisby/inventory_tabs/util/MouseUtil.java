package folk.sisby.inventory_tabs.util;

import java.nio.DoubleBuffer;

import com.mojang.blaze3d.platform.InputUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;

public class MouseUtil {
    private static double mouseX = -1D, mouseY = -1D;

    public static void push() {
        mouseX = getMouseX();
        mouseY = getMouseY();
    }

    public static void tryPop() {
        if (mouseX != -1D && mouseY != -1D) {
            InputUtil.setCursorParameters(MinecraftClient.getInstance().getWindow().getHandle(), 212993, mouseX,
                    mouseY);

            mouseX = -1D;
            mouseY = -1D;
        }
    }

    public static double getMouseX() {
        DoubleBuffer mouseBuf = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(MinecraftClient.getInstance().getWindow().getHandle(), mouseBuf, null);

        return mouseBuf.get(0);
    }

    public static double getMouseY() {
        DoubleBuffer mouseBuf = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(MinecraftClient.getInstance().getWindow().getHandle(), null, mouseBuf);

        return mouseBuf.get(0);
    }
}
