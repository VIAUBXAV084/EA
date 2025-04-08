package hu.bme.kszk.service

import hu.bme.kszk.service.rooms.blocked.BlockedRoomsService
import hu.bme.kszk.service.rooms.blocked.BlockedRoomsServiceInterface
import hu.bme.kszk.service.rooms.hidden.HiddenRoomService
import hu.bme.kszk.service.rooms.hidden.HiddenRoomServiceInterface
import hu.bme.kszk.service.rooms.regular.RoomsService
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface

import hu.bme.kszk.service.rooms.reports.RoomReportsService
import hu.bme.kszk.service.rooms.reports.RoomReportsServiceInterface
import hu.bme.kszk.service.rooms.roominviterequest.RoomInviteRequestsServiceInterface
import hu.bme.kszk.service.rooms.roominviterequest.RoomInviteRequestsService
import hu.bme.kszk.service.usecases.UsecasesService
import hu.bme.kszk.service.usecases.UsecasesServiceInterface
import hu.bme.kszk.service.rooms.roommergeapprove.RoomMergeApprovesService
import hu.bme.kszk.service.rooms.roommergerequest.RoomMergeRequestServiceInterface
import hu.bme.kszk.service.rooms.roommergerequest.RoomMergeRequestService
import hu.bme.kszk.service.users.admins.AdminService
import hu.bme.kszk.service.users.admins.AdminsServiceInterface
import hu.bme.kszk.service.users.blockedusers.BlockedUsersServiceInterface
import hu.bme.kszk.service.users.blockedusers.BlockedUsersService
import hu.bme.kszk.service.users.bros.BroService
import hu.bme.kszk.service.users.bros.BroServiceInterface
import hu.bme.kszk.service.users.contactinfos.ContactInfoService
import hu.bme.kszk.service.users.userpreferences.UserPreferencesService
import hu.bme.kszk.service.users.userreports.UserReportsService
import hu.bme.kszk.service.users.userreports.UserReportsServiceInterface
import hu.bme.kszk.service.users.users.UsersService
import hu.bme.kszk.service.users.users.UsersServiceInterface

data object MainService : MainServiceInterface {
    override val roomsService: RoomsServiceInterface by lazy {
        RoomsService()
    }
    override val blockedRoomsService: BlockedRoomsServiceInterface by lazy {
        BlockedRoomsService()
    }

    override val roomReportsService: RoomReportsServiceInterface by lazy {
        RoomReportsService()
    }
    override val hiddenRoomService: HiddenRoomServiceInterface by lazy {
        HiddenRoomService()
    }


    override val roomMergeApprovesService: RoomMergeApprovesService by lazy {
        RoomMergeApprovesService()
    }
    override val roomMergeRequestService: RoomMergeRequestServiceInterface by lazy {
        RoomMergeRequestService()
    }
    override val roomInviteRequestsService: RoomInviteRequestsServiceInterface by lazy {
        RoomInviteRequestsService()
    }
    override val usersService: UsersServiceInterface by lazy {
        UsersService()
    }
    override val contactInfoService: ContactInfoService by lazy {
        ContactInfoService()
    }
    override val adminService: AdminsServiceInterface by lazy {
        AdminService()
    }
    override val broService: BroServiceInterface by lazy {
        BroService()
    }
    override val usersPreferencesService: UserPreferencesService by lazy {
        UserPreferencesService()
    }
    override val userReportsService: UserReportsServiceInterface by lazy {
        UserReportsService()
    }
    override val blockedUsersService: BlockedUsersServiceInterface by lazy {
        BlockedUsersService()
    }


    override val usecasesService: UsecasesServiceInterface by lazy {
        UsecasesService()
    }
}