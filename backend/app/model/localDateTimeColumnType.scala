package model

import java.sql.Timestamp
import java.time.LocalDateTime
import slick.jdbc.MySQLProfile.api._

trait CustomColumnTypes {
  
  implicit val localDateTimeColumnType = MappedColumnType.base[LocalDateTime, Timestamp](
    ldt => Timestamp.valueOf(ldt),
    ts => ts.toLocalDateTime
  )
}
