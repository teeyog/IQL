package iql.spark.listener

import org.apache.spark.sql.streaming.StreamingQueryListener

class IQLStreamingQueryListener(f: (StreamingQueryListener.QueryStartedEvent, StreamingQueryListener.QueryTerminatedEvent) => Any) extends StreamingQueryListener {
    var queryStartEvent: StreamingQueryListener.QueryStartedEvent = _

    override def onQueryStarted(event: StreamingQueryListener.QueryStartedEvent): Unit = {
        queryStartEvent = event
    }

    override def onQueryProgress(event: StreamingQueryListener.QueryProgressEvent): Unit = {}

    override def onQueryTerminated(event: StreamingQueryListener.QueryTerminatedEvent): Unit = {
        f(queryStartEvent, event)
    }
}
