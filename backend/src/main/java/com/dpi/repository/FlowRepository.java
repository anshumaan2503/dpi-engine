package com.dpi.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dpi.entity.FlowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface FlowRepository extends JpaRepository<FlowEntity, Long> {

    Optional<FlowEntity> findByFlowKey(String flowKey);

    List<FlowEntity> findByFlowKeyIn(List<String> flowKeys);

    @Modifying
    @Transactional
    @Query(value = """
    INSERT INTO flows (
        flow_key, src_ip, dst_ip, src_port, dst_port, protocol,
        domain, app_type, packet_count, byte_count,
        start_time, end_time, blocked
    )
    VALUES (
        :flowKey, :srcIp, :dstIp, :srcPort, :dstPort, :protocol,
        :domain, :appType, :packetCount, :byteCount,
        :startTime, :endTime, :blocked
    )
    ON CONFLICT (flow_key)
    DO UPDATE SET
        packet_count = EXCLUDED.packet_count,
        byte_count = EXCLUDED.byte_count,
        end_time = EXCLUDED.end_time,
        domain = EXCLUDED.domain,
        app_type = EXCLUDED.app_type,
        blocked = EXCLUDED.blocked
    """, nativeQuery = true)
    void upsertFlow(
            @Param("flowKey") String flowKey,
            @Param("srcIp") String srcIp,
            @Param("dstIp") String dstIp,
            @Param("srcPort") int srcPort,
            @Param("dstPort") int dstPort,
            @Param("protocol") String protocol,
            @Param("domain") String domain,
            @Param("appType") String appType,
            @Param("packetCount") long packetCount,
            @Param("byteCount") long byteCount,
            @Param("startTime") long startTime,
            @Param("endTime") long endTime,
            @Param("blocked") boolean blocked
    );
}