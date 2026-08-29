# 注释后代码高亮不立即刷新 修复方案

## Repository Research（已核查结论）

### 现象根因
用户在 Eclipse 中按注释快捷键（HTML `<!-- -->`/JS/CSS `/* */` 或 JS `//`）后：**注释区域仍然按原代码的语法分区着色**，必须：
- 切换编辑器 tab 再切回来；
- 或再任意输入一个字符；
- 或保存文件 → 高亮才更新。

### 调用链
三个编辑器 × 三条触发路径都使用 `IDocument.replace(offset, length, text)` 直接改文档内容：
1. 命令 → `ToggleCommentHandler.toggleComment(HTMLSourceEditor editor)` / `toggleJSComment` / `toggleCSSComment`
   - [ToggleCommentHandler.java](file:///Users/chenqian/Documents/TuLinv8_All/tlv8-plugins/com.tulin.v8.webtools.ide/src/com/tulin/v8/webtools/ide/handlers/ToggleCommentHandler.java#L65-L131)
2. `MultilineCommentHandler` / `UnMultilineCommentHandler` 复用 `ToggleCommentHandler` + 自身 JS/CSS 内联方法
   - [MultilineCommentHandler.java](file:///Users/chenqian/Documents/TuLinv8_All/tlv8-plugins/com.tulin.v8.webtools.ide/src/com/tulin/v8/webtools/ide/handlers/MultilineCommentHandler.java#L65-L97)
   - [UnMultilineCommentHandler.java](file:///Users/chenqian/Documents/TuLinv8_All/tlv8-plugins/com.tulin.v8.webtools.ide/src/com/tulin/v8/webtools/ide/handlers/UnMultilineCommentHandler.java#L65-L98)
3. 编辑器内局部 `CommentAction.run()`（JS / CSS 编辑器，以及 HTMLSourceEditor.CommentAction 调用 ToggleCommentHandler）
   - [HTMLSourceEditor.java#L613-L624](file:///Users/chenqian/Documents/TuLinv8_All/tlv8-plugins/com.tulin.v8.webtools.ide/src/com/tulin/v8/webtools/ide/html/editors/HTMLSourceEditor.java#L613-L624)
   - [CSSEditor.java#L159-L186](file:///Users/chenqian/Documents/TuLinv8_All/tlv8-plugins/com.tulin.v8.webtools.ide/src/com/tulin/v8/webtools/ide/css/editors/CSSEditor.java#L159-L186)
   - [JavaScriptEditor.java#L343-L369](file:///Users/chenqian/Documents/TuLinv8_All/tlv8-plugins/com.tulin.v8.webtools.ide/src/com/tulin/v8/webtools/ide/js/editors/JavaScriptEditor.java#L343-L369)

### Bug 原因（多层叠加，任一都能导致高亮不刷新）

1. **FastPartitioner 分区缓存未 invalidate 导致文档分区视图过时**（核心）：
   文档的分区（`HTML_COMMENT` / `JAVASCRIPT` / `HTML_TAG`）由 `FastPartitioner` 以 **位置序列 + 长度** 形式缓存。注释操作在文档中间插入 `<!--`/`-->` 或 `/*`/`*/`，如果替换的范围**横跨了两个相邻的旧分区边界**，FastPartitioner 的 `documentChanged()` 不会把前一个分区的起点回退到「受影响最小位置」去重扫 → 新的 `<!--` 仍被当成 DEFAULT_CONTENT → 没有切到 HTML_COMMENT 分区类型 → presentation reconciler 使用「旧分区」调用对应的 damager/repairer → 注释颜色不出现。这是 Eclipse 早期 FastPartitioner 的已知缺陷（JFace 新版有 IDocumentPartitionerExtension3.invalidatePresentation/damageRegion 但代码里没触发）。

2. **PresentationReconciler 的 damager 计算结果被 partition 边界截断**：
   `HTMLTagDamagerRepairer.getDamageRegion()` 在 `documentPartitioningChanged=false` 时只找前后 `<` / `>`，对 HTML_COMMENT 这种跨分区的损坏范围**只返回当前 partition** → 注释开闭字符落在旧分区上，完全没被 damage → 不 repaint。

3. **Handler 执行后不主动 invalidate presentation**：手动重绘保险层缺失。即使分区正确，也会因为 damager 计算不正确导致不刷新。

### 架构上的「修复最佳实践」参考（JFace Text 3.x 标准做法）
在「程序式修改文档（非用户键盘输入）」场景下，JFace 官方做法是**改完文档后显式做三件事**：
1. 把 partitioner 在「从文档首个受影响分区起点 到 整个改动+新文本尾」范围 invalidate（必要时）；
2. 让 `ISourceViewer.invalidateTextPresentation(offset, length)` 把这段外观标记 dirty，强制 PresentationReconciler 对整个修改+前后邻接分区重绘；
3. 对 `IDocumentExtension3` 支持的分区（HTML 是 HTMLPartitionScanner 命名）也按命名分区做一次，避免 DEFAULT/扩展两条路径漏掉。

## Files and Modules（改动范围）

**Handlers 层（统一工具方法，调用改文档→刷新）**
- `src/com/tulin/v8/webtools/ide/handlers/ToggleCommentHandler.java`：
  - 新增 `afterDocChanged(ISourceViewer viewer, IDocument doc, int offset, int oldLen, int newLen)` 静态工具方法：
    1. 扩一段安全范围（`offset` 前后各 80 字符，覆盖 `<!--` 到 `-->` 以及前后相邻标签），
    2. `viewer.invalidateTextPresentation(safeOffset, safeLen)`，
    3. 对 `IDocumentPartitioner`/`IDocumentExtension3` 做条件 `invalidate(safeOffset, safeLen)`（反射调用，兼容不同 Eclipse 版本），
    4. 必要时 `Display.getDefault().asyncExec()` 的 viewer.redraw() 保险（SWT 线程）。
  - `toggleComment/toggleJSComment/toggleCSSComment` 每次 `doc.replace()` 后都调一次。

- `src/com/tulin/v8/webtools/ide/handlers/MultilineCommentHandler.java`：
  - JSComment / CSSComment → 用 `viewer = editor.getSourceViewer()` 获取后，同样 afterDocChanged。
- `src/com/tulin/v8/webtools/ide/handlers/UnMultilineCommentHandler.java`：同上。

**编辑器内 CommentAction 层（独立 run 方法的那些，不是用 Handler 的内联实现）**
- `src/com/tulin/v8/webtools/ide/js/editors/JavaScriptEditor.java` → `CommentAction.run()` doc.replace 后 afterDocChanged。
- `src/com/tulin/v8/webtools/ide/css/editors/CSSEditor.java` → `CommentAction.run()` 同上。

> 注：HTMLSourceEditor 的 CommentAction 只是 `ToggleCommentHandler.toggleComment(this)`，会被上面 Handler 层统一覆盖。

## Implementation Steps

1. **写 RED 测试**：新增 `tests/com/tulin/v8/webtools/ide/syntax/P05_CommentHighlightRefreshTest.java`。
   - 场景：构造最小 Document，装 FastPartitioner+HTMLPartitionScanner，用 ToggleCommentHandler 相同算法对一个 span 加 `<!--`/`-->` 包装替换；
   - **断言 1**（分区刷新）：替换后调用我们的 afterDocChanged，`HTMLPartitionScanner` 的分区在改动 span 的偏移上**必须是 HTML_COMMENT**（不调就是 RED，因为旧分区缓存未失效仍是 DEFAULT_CONTENT）；
   - **断言 2**（JS 场景）：JS `/* comment */` 包装替换后 JavaScript 分区对应偏移类型正确；
   - 这两个断言先跑出 RED（不调用 afterDocChanged 的 baseline）→ 再调用修复代码 GREEN。

2. **抽取工具方法**：`ToggleCommentHandler.afterDocChanged(ISourceViewer, IDocument, offset, oldLen, newLen)`。
   - 对不存在的 Eclipse 接口（IDocumentPartitionerExtension3）用反射 try/catch，保持跨版本兼容。
   - safeOffset=max(0, offset-200)，safeLen=min(docLen-safeOffset, newLen+400)；200 保证前后分区（如 `<script>` 开闭、`<div>` 开闭）也重画。

3. **ToggleCommentHandler 三方法接入**：
   - toggleComment/toggleJSComment/toggleCSSComment 的三处 doc.replace 都取 viewer（editor.getSourceViewer()，如果是 HTMLSourceEditor 直接 getViewer 或 getSourceViewer）。
   - XMLEditor 继承 HTMLSourceEditor 一样能用。

4. **Multiline/UnMultiline 两个 handler 的 JSComment/CSSComment**：改后调用 afterDocChanged。

5. **JavaScriptEditor、CSSEditor CommentAction**：改后调用 afterDocChanged（复用 ToggleCommentHandler 方法即可，方法是 public static）。

6. **GREEN + 回归**：P05 RED→GREEN；P02/P03/P04 回归继续 GREEN（我们没碰 formatter、beautify.js 等，回归预期全过）。

## Dependencies and Considerations

- **跨 Eclipse 版本兼容**：`IDocumentPartitionerExtension3.invalidate(int,int)` 是 3.5 后的 API；旧版本只定义了 `IDocumentPartitioner.disconnect/connect`。用反射：先 try extension3，失败 fallback disconnect()+reconnect 整个 partitioner（成本高但可靠，注释操作是单次所以可接受）。
- **ISourceViewer 非 UI 线程安全**：afterDocChanged 本身在 command handler 里执行，默认是 UI 线程（Handler.execute 来自 Workbench）。稳妥起见再包一层 Display.getDefault().syncExec if !Display.getCurrent()。
- **JS 单行注释 `//`**：partition 不会变化（仍然 `JAVASCRIPT` / DEFAULT_CONTENT），但 damager repairer 需要重算扫描 token，也同样调用 invalidateTextPresentation 足够。
- **不要误 invalidate 全文档**：会造成整屏闪烁。safeLen 控制在「改动 + 400」可以避免全局重绘。

## Validation

- **P05_CommentHighlightRefreshTest**：
  1. `<!-- -->` 加注释 → afterDocChanged → partition 类型=HTML_COMMENT（RED→GREEN）
  2. `/* */` 加注释 → afterDocChanged → partition=JS/CS SCRIPT/comment（RED→GREEN）
  3. 不调用 afterDocChanged 的 control 组：断言 **PASS（即 partition 还是旧类型 = 重现 bug）** 确保 test 敏感度。
- **回归**：P02/P03/P04 继续 GREEN。
- **静态**：Handler 修改处的 doc.replace 之后都有 afterDocChanged 调用（计数核对：共 ToggleCommentHandler 3 处 + MultilineCommentHandler 2×1 处 + UnMultilineCommentHandler 2×1 处 + JavaScriptEditor 1 + CSSEditor 1 = 共 8 处全部核对）。

## Risks

| 风险 | 应对 |
|------|------|
| 反射调不到 invalidate → 在旧 Eclipse 上仍然只调 viewer.invalidateTextPresentation 也足够让 DefaultDamagerRepairer 重扫当前行；最坏 fallback disconnect+connect partitioner 后再 invalidate。 |
| afterDocChanged 范围过小 → 前面 200 后面 400；经验上足以覆盖 `<!DOCTYPE html>` + `<script src="...">` + 注释开闭标签。如果外部脚本 `<script>` 与注释相邻很长可以再调。 |
| invalidateTextPresentation 引起闪烁 → 只对改动 region 调，不调 0,len。闪烁只在该 range，正常用户察觉不到。 |
| getSourceViewer 在 editor 子类没暴露 → HTMLSourceEditor 有 public getViewer()，JavaScriptEditor 可以通过 `getSourceViewer()`（protected 在 TextEditor）用 getAdapter 或 cast 访问。稳妥做法：新增公共 API `WebSourceEditor.getSourceViewerForHandlers()` 供 handler 拿。 |
