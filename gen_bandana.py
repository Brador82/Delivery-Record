from PIL import Image, ImageDraw
import math

size = 256
img = Image.new('RGBA', (size, size), (139, 0, 0, 255))
draw = ImageDraw.Draw(img)

white = (255, 255, 255, 200)
cream = (255, 240, 220, 140)

# Diagonal cross-hatch
for i in range(-size, size*2, 32):
    draw.line([(i, 0), (i+size, size)], fill=(120, 10, 10, 180), width=1)
    draw.line([(i, size), (i+size, 0)], fill=(120, 10, 10, 180), width=1)

cx, cy = size//2, size//2

# Central medallion
for d, c in [(80, white), (65, cream)]:
    pts = [(cx, cy-d), (cx+d, cy), (cx, cy+d), (cx-d, cy)]
    draw.polygon(pts, outline=c, fill=None)

draw.ellipse([cx-40, cy-40, cx+40, cy+40], outline=white, width=2)
draw.ellipse([cx-25, cy-25, cx+25, cy+25], outline=cream, width=1)
draw.ellipse([cx-8, cy-8, cx+8, cy+8], fill=white)

for angle_deg in range(0, 360, 45):
    a = math.radians(angle_deg)
    dx = int(cx + 32 * math.cos(a))
    dy = int(cy + 32 * math.sin(a))
    draw.ellipse([dx-3, dy-3, dx+3, dy+3], fill=white)

# Corner medallions
for corner_x, corner_y in [(0, 0), (size, 0), (0, size), (size, size)]:
    for d, c in [(45, cream), (35, white)]:
        pts = [(corner_x, corner_y-d), (corner_x+d, corner_y), (corner_x, corner_y+d), (corner_x-d, corner_y)]
        draw.polygon(pts, outline=c, fill=None)
    draw.ellipse([corner_x-20, corner_y-20, corner_x+20, corner_y+20], outline=white, width=1)
    draw.ellipse([corner_x-6, corner_y-6, corner_x+6, corner_y+6], fill=cream)
    for angle_deg in range(0, 360, 60):
        a = math.radians(angle_deg)
        dx = int(corner_x + 28 * math.cos(a))
        dy = int(corner_y + 28 * math.sin(a))
        draw.ellipse([dx-2, dy-2, dx+2, dy+2], fill=cream)

# Edge medallions
for mx, my in [(cx, 0), (0, cy), (size, cy), (cx, size)]:
    draw.ellipse([mx-15, my-15, mx+15, my+15], outline=white, width=1)
    draw.ellipse([mx-5, my-5, mx+5, my+5], fill=cream)
    for angle_deg in range(0, 360, 90):
        a = math.radians(angle_deg)
        dx = int(mx + 10 * math.cos(a))
        dy = int(my + 10 * math.sin(a))
        draw.ellipse([dx-2, dy-2, dx+2, dy+2], fill=white)

out = r'c:\_WORKSPACE_\PROJECTS\Daily Record\app\src\main\res\drawable-xxhdpi\bg_bandana_tile.png'
img.save(out, 'PNG')
print(f'Saved {out}')
