package org.travel.service;

import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;
import org.jxmpp.jid.parts.Localpart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.travel.config.OpenfireConfig;
import org.travel.exception.LoginException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class XMPPService {
    private final Map<Long, AbstractXMPPConnection> activeUserConnections = new ConcurrentHashMap<>();

    @Autowired
    private OpenfireConfig openfireConfig;

    public void xmppLogin(Long uid,String password){
        AbstractXMPPConnection connection = null;
        log.info("尝试登录 - 用户名: u{}, 密码: {}", uid, password);
        try{
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration
                    .builder()
                    .setHost(openfireConfig.getHost())
                    .setPort(openfireConfig.getPort())
                    .setXmppDomain(openfireConfig.getDomain())
                    .setUsernameAndPassword(Localpart.from("u" + uid),password)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.valueOf(openfireConfig.getSecurityMode()))
                    .setSendPresence(true) //自动发送在线状态
                    .build();

            connection = new XMPPTCPConnection(config);
            connection.connect();
            connection.login();

            configConnection(connection);
            addConnectionListener(connection, uid);
            activeUserConnections.put(uid,connection);
        }catch (Exception e){
            log.error("❌ XMPP登录失败: uid={}, 错误详情: {}", uid, e.getMessage(), e);
            if(connection != null) connection.disconnect();
            throw LoginException.xmppLoginFailed();
        }
    }

    private void configConnection(AbstractXMPPConnection connection){
        // === Smack 自动管理配置 ===
        ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
        reconnectionManager.enableAutomaticReconnection();
        reconnectionManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.RANDOM_INCREASING_DELAY);

        // 自动Ping保活
        PingManager pingManager = PingManager.getInstanceFor(connection);
        pingManager.setPingInterval(120); // 2分钟ping一次

        // 自动回复ping
        pingManager.registerPingFailedListener(new PingFailedListener() {
            @Override
            public void pingFailed() {
                log.warn("Ping失败，连接可能已断开");
            }
        });
    }

    // 添加连接监听器
    private void addConnectionListener(AbstractXMPPConnection connection,Long uid) {
        connection.addConnectionListener(new ConnectionListener() {
            @Override
            public void connectionClosed() {
                log.info("XMPP连接正常关闭: uid={}", uid);
                activeUserConnections.remove(uid);
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                log.error("XMPP连接异常断开: uid={}", uid, e);
//                activeUserConnections.remove(uid);
            }
        });
    }

    public void xmppLogout(Long uid) {
        AbstractXMPPConnection connection = activeUserConnections.remove(uid);
        if(connection != null && connection.isConnected()){
            connection.disconnect();
            log.info("✅ XMPP退出成功: uid={}", uid);
        }
    }
}
