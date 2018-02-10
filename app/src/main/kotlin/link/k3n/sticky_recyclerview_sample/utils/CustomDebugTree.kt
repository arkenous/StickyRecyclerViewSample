package link.k3n.sticky_recyclerview_sample.utils

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import timber.log.Timber

/**
 * Timber custom debug tree class which can jump to source from debug log.
 * @author Yoshinori Isogai
 * @see <a href="https://qiita.com/shiraji/items/5815bfe667d042051119">Original Qiita post</a>
 */
class CustomDebugTree : Timber.DebugTree() {
    companion object {
        const val MAX_LOG_LENGTH = 4000
        const val CALLER_INFO_FORMAT = " at %s(%s:%s)"
    }
    private var mShowLink = true
    private var mCallerInfo = ""

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (mShowLink) mCallerInfo = getCallerInfo(Throwable().stackTrace)

        if (message.length < MAX_LOG_LENGTH) printSingleLine(priority, tag, message + mCallerInfo)
        else printMultipleLines(priority, tag, message)
    }

    private fun printMultipleLines(priority: Int, tag: String?, message: String) {
        // Split by line, then ensure each line can fit into Log's maximum length

        var counter = 0
        val length = message.length
        while (counter < length) {
            var newline = message.indexOf('\n', counter)
            newline = if (newline != -1) newline else length
            while (counter < newline) {
                val end = Math.min(newline, counter + MAX_LOG_LENGTH)
                val part = message.substring(counter, end)
                printSingleLine(priority, tag, part)
                counter = end
            }
            ++counter
        }

        if (mShowLink && !TextUtils.isEmpty(mCallerInfo)) printSingleLine(priority, tag, message)
    }

    @SuppressLint("LogNotTimber")
    private fun printSingleLine(priority: Int, tag: String?, message: String) {
        if (priority == Log.ASSERT) Log.wtf(tag, message)
        else Log.println(priority, tag, message)
    }

    private fun getCallerInfo(stacks : Array<StackTraceElement>?) : String {
        // Are you using proguard??
        if (stacks == null || stacks.size < 5) return "";

        return formatForLogCat(stacks[5])
    }

    private fun formatForLogCat(stack : StackTraceElement) : String {
        val className = stack.className
        val packageName = className.substring(0, className.lastIndexOf("."))
        return String.format(CALLER_INFO_FORMAT, packageName, stack.fileName, stack.lineNumber)
    }
}