/* Capture the application's DOM, preserving its content and layout for design figures.
 * Run against a fresh seeded database: this script registers the example member.
 */
const { chromium } = require(process.env.PLAYWRIGHT_MODULE || 'playwright');
const fs = require('fs');
const path = require('path');
const crypto = require('crypto');
const assert = require('assert/strict');
process.chdir(path.join(__dirname, '..'));
const base = process.env.CINEMAN_BASE_URL || 'http://127.0.0.1:8080';
const out = process.env.CINEMAN_CAPTURE_DIR || 'docs/ui-design/screens';
const evidence = process.env.CINEMAN_EVIDENCE_DIR || 'docs/evidence';
fs.mkdirSync(out, { recursive: true });
fs.mkdirSync(evidence, { recursive: true });
const monochrome = `
  * {color:#111!important; background-color:transparent!important;
     border-color:#777!important; box-shadow:none!important; text-shadow:none!important}
  body,header,footer,.card,.metrics section,input {background-color:#fff!important}
  button,.button,th {background-color:#eee!important}
`;
const results = [], checks = [];
function check(value, label) { assert(value, label); checks.push('PASS ' + label); }
(async () => {
  const browser = await chromium.launch({
    headless: true, executablePath: process.env.CHROMIUM_PATH || '/usr/bin/chromium',
    args: ['--no-sandbox', '--lang=vi-VN']
  });
  const context = await browser.newContext({
    viewport: { width: 1280, height: 900 }, deviceScaleFactor: 2,
    locale: 'vi-VN', bypassCSP: true
  });
  const page = await context.newPage();
  async function snapshot() {
    return page.evaluate(() => ({
      text: document.body.innerText,
      elements: [...document.querySelectorAll('input,button,a,th,td,h1,h2,.card,.metrics section')]
        .map(el => {
          const r = el.getBoundingClientRect();
          return { tag: el.tagName, text: el.innerText || '', name: el.getAttribute('name'),
            type: el.getAttribute('type'), href: el.getAttribute('href'), value: el.value || '',
            x: r.x, y: r.y, width: r.width, height: r.height };
        })
    }));
  }
  async function capture(stem) {
    await page.locator('body').click({ position: { x: 2, y: 2 } });
    await page.evaluate(() => document.fonts.ready);
    await page.evaluate(() => window.scrollTo(0, 0));
    await page.screenshot({ path: path.join(evidence, stem + '.png'), fullPage: true });
    const before = await snapshot();
    const style = await page.addStyleTag({ content: monochrome });
    const after = await snapshot();
    assert.deepEqual(after, before, stem + ': CSS must preserve all text, controls and geometry');
    // Crop only empty space below main; include header and all actual main content.
    const bottom = await page.locator('main').evaluate(el => Math.max(...[...el.children]
      .map(c => c.getBoundingClientRect().bottom + window.scrollY)));
    await page.screenshot({ path: path.join(out, stem + '.png'), fullPage: true,
      clip: { x: 0, y: 0, width: 1280, height: Math.ceil(bottom + 24) } });
    const png = fs.readFileSync(path.join(out, stem + '.png'));
    const pixels = [png.readUInt32BE(16), png.readUInt32BE(20)];
    assert.deepEqual(pixels, [2560, Math.ceil(bottom + 24) * 2],
      stem + ': image must include all main content beyond the viewport');
    results.push({ screen: stem, route: page.url().replace(base, ''),
      text: after.text, controls: after.elements.filter(e => !['TD','TH'].includes(e.tag))
        .map(({value, ...e}) => e),
      headers: await page.locator('th').allTextContents(),
      rows: await page.locator('tbody tr').allTextContents(),
      css_preserves_dom_and_geometry: true,
      complete_main_content: true, pixel_dimensions: pixels,
      image_sha256: crypto.createHash('sha256').update(png).digest('hex') });
    await style.evaluate(el => el.remove());
  }
  await page.goto(base + '/'); await capture('01-home');
  await page.getByRole('link', { name: 'Đăng ký thành viên →' }).click();
  const values = { fullName:'Nguyễn Văn An', phone:'0901234567', email:'an@example.com',
    username:'nguyenan', password:'Abcd@123', confirmPassword:'Abcd@123' };
  for (const [k,v] of Object.entries(values)) await page.locator('[name='+k+']').fill(v);
  check(await page.locator('main input:not([type=hidden])').count() === 6, 'registration has six actual inputs');
  await capture('02-register');
  await page.getByRole('button', { name:'Đăng ký', exact:true }).click();
  await page.waitForURL('**/registered');
  check(await page.getByText('KH1', {exact:true}).isVisible(), 'fresh registration returns KH1');
  await capture('03-registered');
  await page.goto(base+'/register');
  for (const [k,v] of Object.entries(values)) await page.locator('[name='+k+']').fill(v);
  await page.getByRole('button', { name:'Đăng ký', exact:true }).click();
  check(await page.getByRole('alert').isVisible(), 'duplicate error stays on registration form');
  check(await page.locator('[name=password]').inputValue() === '', 'duplicate clears password');
  check(await page.locator('[name=fullName]').inputValue() === values.fullName, 'duplicate retains name');
  await capture('04-duplicate');
  await page.goto(base+'/manager/reports');
  check(page.url().endsWith('/login'), 'unauthenticated manager access redirects');
  await page.locator('[name=username]').fill('manager');
  await page.locator('[name=password]').fill('wrong');
  await page.getByRole('button', {name:'Đăng nhập',exact:true}).click();
  check(await page.getByRole('alert').isVisible(), 'wrong manager password rejected');
  await capture('05-login');
  await page.locator('[name=password]').fill('Manager@123');
  await page.getByRole('button', {name:'Đăng nhập',exact:true}).click();
  await page.waitForURL('**/manager'); await capture('06-manager');
  await page.getByRole('link',{name:'Xem báo cáo →'}).click();
  check(await page.locator('select,input:not([type=hidden])').count() === 0, 'report type is a card with a link, no selection control');
  await capture('07-report-type');
  await page.getByRole('link',{name:'Tiếp tục →'}).click();
  await page.locator('[name=from]').fill('2026-11-01');
  await page.locator('[name=to]').fill('2026-11-30'); await capture('08-date-range');
  await page.getByRole('button',{name:'Xem thống kê'}).click();
  check((await page.locator('.metrics strong').allTextContents()).join('|') === '750.000 ₫|9|2', 'movie metrics are 750000, 9 tickets and 2 films');
  check(await page.locator('th').count() === 5, 'movie table has five actual columns');
  await capture('09-movies');
  await page.getByRole('link',{name:'Xem suất chiếu →'}).first().click();
  check(await page.locator('th').count() === 6, 'showtime table has six actual columns');
  check((await page.locator('.total').innerText()).includes('480.000'), 'showtime sum is 480000');
  await capture('10-showtimes');
  await page.getByRole('link',{name:'Xem hóa đơn →'}).first().click();
  check(await page.locator('th').count() === 5, 'invoice table has five actual columns');
  check((await page.locator('.total').innerText()).includes('160.000'), 'invoice sum is 160000');
  await capture('11-invoices');
  await page.getByRole('link',{name:'← Các suất chiếu'}).click();
  check(page.url().includes('from=2026-11-01') && page.url().includes('to=2026-11-30'), 'back retains period');
  await page.goto(base+'/manager/reports?from=2027-01-01&to=2027-01-31');
  check(await page.getByText('Không có doanh thu trong khoảng ngày đã chọn.').isVisible(), 'empty period has an explanatory table row');
  check((await page.locator('.metrics strong').allTextContents()).join('|') === '0 ₫|0|0', 'empty period metrics are zero');
  await capture('12-empty');
  const response = await page.goto(base+'/manager/reports?from=2026-11-30&to=2026-11-01');
  check(response.status() === 400, 'invalid date range returns HTTP 400');
  check(await page.locator('input[type=date]').count() === 0, 'invalid date range uses a separate error page');
  check(await page.getByRole('link',{name:'Chọn lại khoảng ngày'}).getAttribute('href') === '/manager/reports', 'date error offers return link to blank filter');
  await capture('13-invalid-range');
  const wrongContext = await page.goto(base+'/manager/invoices?from=2026-11-01&to=2026-11-30&movieId=2&showtimeId=1');
  check(wrongContext.status() === 400, 'mismatched showtime rejected');
  check((await context.request.post(base+'/register',{form:values})).status() === 403, 'CSRF missing rejected');
  await page.goto(base+'/manager'); await page.getByRole('button',{name:'Đăng xuất'}).click();
  await page.goto(base+'/manager'); check(page.url().endsWith('/login'), 'logout revokes manager access');
  const sources = ['src/main/java/vn/cineman/web/AppServlet0.java','src/main/java/vn/cineman/web/View0.java',
    'src/main/resources/style.css','src/main/resources/seed.sql'];
  fs.writeFileSync(path.join(path.dirname(out),'capture-manifest.json'), JSON.stringify({
    method:'Actual server-rendered DOM; monochrome CSS changes color only; text, control attributes and bounding boxes compared before/after',
    source_sha256:Object.fromEntries(sources.map(p=>[p,crypto.createHash('sha256').update(fs.readFileSync(p)).digest('hex')])),
    screens:results, checks
  },null,2)+'\n');
  fs.writeFileSync(path.join(path.dirname(out),'capture-checks.txt'),checks.join('\n')+'\nTOTAL '+checks.length+' passed\n');
  console.log(checks.join('\n')+'\nTOTAL '+checks.length+' passed; '+results.length+' screens captured from unchanged DOM');
  await browser.close();
})().catch(e=>{console.error(e);process.exit(1)});
