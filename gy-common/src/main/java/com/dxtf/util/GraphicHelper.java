package com.dxtf.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

public class GraphicHelper {

	public static final String VERIFY_CODES = "0123456789";

	/**
	 * 以字符串形式返回生成的验证码，同时输出一个图片
	 *
	 * @param width
	 *            图片的宽度
	 * @param height
	 *            图片的高度
	 * @param imgType
	 *            图片的类型
	 * @param output
	 *            图片的输出流(图片将输出到这个流中)
	 * @return 返回所生成的验证码(字符串)
	 */
	public static String createImge(final int width, final int height, final String imgType, OutputStream output) {
		// 填充的字符串
		// 缓存生成的验证码
		StringBuffer sb = new StringBuffer();
		// 随机数
		Random rand = new Random();
		// 新建RGB图片
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 创建载体 Graphics2D 映射集
		Graphics2D g2 = image.createGraphics();
		// 开启抗锯齿微调
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// 规定的数字颜色
		Color[] colors = new Color[5];
		// 颜色集合
		Color[] colorSpaces = new Color[] { Color.WHITE, Color.CYAN, Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA,
				Color.ORANGE, Color.PINK, Color.YELLOW };
		// 声明数字颜色顺序数组
		float[] fractions = new float[colors.length];
		// 随机匹配颜色
		for (int i = 0; i < colors.length; i++) {
			colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
			fractions[i] = rand.nextFloat();
		}
		Arrays.sort(fractions);
		// 设置边框色
		g2.setColor(Color.GRAY);
		g2.fillRect(0, 0, width, height);
		Color c = getRandColor(200, 250);
		// 设置背景色
		g2.setColor(c);
		g2.fillRect(0, 2, width, height - 4);
		// 绘制干扰线
		Random random = new Random();
		// 设置线条的颜色
		g2.setColor(getRandColor(160, 200));
		for (int i = 0; i < 20; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int xl = random.nextInt(6) + 1;
			int yl = random.nextInt(12) + 1;
			//g2.drawLine(x, y, x + xl + 40, y + yl + 20);
		}
		// 添加噪点
		float yawpRate = 0.05f;
		int area = (int) (yawpRate * width * height);
		for (int i = 0; i < area; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int rgb = getRandomIntColor();
			//image.setRGB(x, y, rgb);
		}
		// 使图片扭曲
		shear(g2, width, height, c);
		g2.setColor(getRandColor(100, 160));
		// 设置字体大小
		int fontSize = height - 4;
		Font font = new Font("Algerian", Font.ITALIC, fontSize);
		g2.setFont(font);
		// 生成4位验证码
		String code = generateVerifyCode(4);
		sb.append(code);
		char[] chars = code.toCharArray();
		for (int i = 0; i < 4; i++) {
			AffineTransform affine = new AffineTransform();
			affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1),
					(width / 4) * i + fontSize / 2, height / 2);
			g2.setTransform(affine);
			// 规定数字位置
			g2.drawChars(chars, i, 1, ((width - 10) / 4) * i + 5, height / 2 + fontSize / 2 - 10);
		}
		// 关闭
		g2.dispose();
		// 将生成的验证码存到服务器
		// 将图片发送给浏览器
		try {
			ImageIO.write(image, imgType, output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 生成随机颜色
	 *
	 * @param fc
	 * @param bc
	 * @return
	 */
	private static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 扭曲
	 *
	 * @param g
	 * @param w1
	 * @param h1
	 * @param color
	 */
	private static void shear(Graphics g, int w1, int h1, Color color) {
		shearX(g, w1, h1, color);
		shearY(g, w1, h1, color);
	}

	/**
	 * 剪切X轴
	 *
	 * @param g
	 * @param w1
	 * @param h1
	 * @param color
	 */
	private static void shearX(Graphics g, int w1, int h1, Color color) {
		Random random = new Random();
		int period = random.nextInt(2);

		boolean borderGap = true;
		int frames = 1;
		int phase = random.nextInt(2);

		for (int i = 0; i < h1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
			g.copyArea(0, i, w1, 1, (int) d, 0);
			if (borderGap) {
				g.setColor(color);
				g.drawLine((int) d, i, 0, i);
				g.drawLine((int) d + w1, i, w1, i);
			}
		}

	}

	/**
	 * 剪切Y轴
	 *
	 * @param g
	 * @param w1
	 * @param h1
	 * @param color
	 */
	private static void shearY(Graphics g, int w1, int h1, Color color) {
		Random random = new Random();
		// 50;
		int period = random.nextInt(40) + 10;
		boolean borderGap = true;
		int frames = 20;
		int phase = 7;
		for (int i = 0; i < w1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
			g.copyArea(i, 0, 1, h1, 0, (int) d);
			if (borderGap) {
				g.setColor(color);
				g.drawLine(i, (int) d, i, 0);
				g.drawLine(i, (int) d + h1, i, h1);
			}

		}

	}

	/**
	 * 生成随机颜色
	 * 
	 * @return
	 */
	private static int getRandomIntColor() {
		Random random = new Random();
		int[] rgb = new int[3];
		int color = 0;
		for (int i = 0; i < 3; i++) {
			rgb[i] = random.nextInt(255);
		}

		for (int c : rgb) {
			color = color << 8;
			color = color | c;
		}
		return color;
	}

	/**
	 * 生成验证码
	 * 
	 * @param verifySize
	 *            验证码长度
	 * @return
	 */
	public static String generateVerifyCode(int verifySize) {
		return generateVerifyCode(verifySize, VERIFY_CODES);
	}

	/**
	 * 使用指定源生成验证码
	 *
	 * @param verifySize
	 *            验证码长度
	 * @param sources
	 *            验证码字符源
	 * @return
	 */
	public static String generateVerifyCode(int verifySize, String sources) {
		if (sources == null || sources.length() == 0) {
			sources = VERIFY_CODES;
		}
		int codesLen = sources.length();
		Random rand = new Random(System.currentTimeMillis());
		StringBuilder verifyCode = new StringBuilder(verifySize);
		for (int i = 0; i < verifySize; i++) {
			verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
		}
		return verifyCode.toString();
	}
}
