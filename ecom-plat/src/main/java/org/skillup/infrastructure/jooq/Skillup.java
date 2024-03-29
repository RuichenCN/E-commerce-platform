/*
 * This file is generated by jOOQ.
 */
package org.skillup.infrastructure.jooq;


import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;
import org.skillup.infrastructure.jooq.tables.Order;
import org.skillup.infrastructure.jooq.tables.Promotion;
import org.skillup.infrastructure.jooq.tables.User;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Skillup extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>skillup</code>
     */
    public static final Skillup SKILLUP = new Skillup();

    /**
     * The table <code>skillup.order</code>.
     */
    public final Order ORDER = Order.ORDER;

    /**
     * The table <code>skillup.promotion</code>.
     */
    public final Promotion PROMOTION = Promotion.PROMOTION;

    /**
     * The table <code>skillup.user</code>.
     */
    public final User USER = User.USER;

    /**
     * No further instances allowed
     */
    private Skillup() {
        super("skillup", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            Order.ORDER,
            Promotion.PROMOTION,
            User.USER);
    }
}
