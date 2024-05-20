package com.gomtak.batch

import org.springframework.batch.item.ItemStreamException
import org.springframework.batch.item.database.JpaPagingItemReader

class CustomJpaPagingItemReader<Person> : JpaPagingItemReader<Person>() {

    @Throws(Exception::class)
    override fun doClose() {
        try {
            // super.doClose() 호출 시 entityManger null 로 에러 발생
            // entityMangerFactory 를 사용하여 entityManger 를 생성하여 close
        } catch (e: Exception) {
            // 추가적인 로그와 예외 처리를 여기에 추가
            System.err.println("Custom close method error: " + e.message)
            e.printStackTrace()
            throw ItemStreamException("Error while closing item reader", e)
        }
    }
}
