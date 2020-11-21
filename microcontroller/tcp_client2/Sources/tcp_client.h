#ifndef TCP_CLIENT_H_
#define TCP_CLIENT_H_

#include "lwip/netif.h" /* struct netif, netif_add, netif_set_up, netif_set_default */
#include "lwip/dhcp.h" /* struct dhcp, dhcp_set_struct, dhcp_start */
#include "lwip/tcp.h" /* struct tcp_pcb, tcp_new, tcp_bind, tcp_write, tcp_output, tcp_close, tcp_connect, TCP_WRITE_FLAG_COPY */
#include "lwip/sys.h" /* sys_init; S32DS (linker) will provide port-specific impl. (e.g., `SDK/middleware/tcpip/tcpip_stack/ports/OS/sys_arch.c`) */
#include "lwip/init.h" /* lwip_init */
#include "lwip/timeouts.h" /* sys_check_timeouts */
#include "lwip/ip_addr.h" /* ip_addr_t, ipaddr_aton, ip_addr_set_zero  */

void start_tcp_client();
err_t create_tcp_connection();

#endif /* TCP_CLIENT_H_ */
